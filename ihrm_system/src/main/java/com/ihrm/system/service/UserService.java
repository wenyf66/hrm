package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.client.CompanyFeignClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.ElementCollection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CompanyFeignClient companyFeignClient;

    /**
     * 根据mobile查询用户
     */
    public User findByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }

    public void save(User user){
        //设置主键
        String id = idWorker.nextId() + "";
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);
        user.setId(id);
        //调用dao保存用户
        userDao.save(user);
    }

    public void update(User user){

        User tempUser = userDao.findById(user.getId()).get();
        tempUser.setUsername(user.getUsername());
        tempUser.setPassword(user.getPassword());
        tempUser.setDepartmentId(user.getDepartmentId());
        tempUser.setDepartmentName(user.getDepartmentName());
        userDao.save(tempUser);
    }

    public User findById(String id){
        return userDao.findById(id).get();
    }

    public void deleteById(String id){
        userDao.deleteById(id);
    }


    public Page<User> findAll(Map<String,Object> map, int page, int size){

        //构造查询条件
        Specification<User> spec = new Specification<User>() {
            // 动态拼接查询条件
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(cb.equal(root.get("departmentId").as(String.class),(String)map.get("departmenId")));
                }

                if (StringUtils.isEmpty("hasDept") ||  ("0").equals((String) map.get("departmenId"))){
                    list.add(cb.isNull(root.get("departmentId")));
                }else {
                    list.add(cb.isNotNull(root.get("departmentId")));
                }

                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> userPage = userDao.findAll(spec, new PageRequest(page, size));
        return userPage;
    }

    /**
     * 分配角色
     * @param userId    用户id
     * @param roleIds   要分配的角色id
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //根据id查询用户
        User user = userDao.findById(userId).get();
        //设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //更新用户
        userDao.save(user);
    }

    /**
     *  批量用户保存
     * @param list  用户list
     * @param companyId 用户所属公司id
     * @param companyName   用户所属公司名称
     */
    @Transactional
    public void saveAll(List<User> list, String companyId, String companyName) {
        for (User user : list) {
            //默认密码
            user.setPassword(new Md5Hash("123456" , user.getMobile() , 3).toString());
            //id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");

            //填充部门的属性
            Department department = companyFeignClient.findByCode(user.getDepartmentId(), companyId);
            if (department != null){
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }

            userDao.save(user);
        }
    }

}

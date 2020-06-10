package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.ElementCollection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private IdWorker idWorker;

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

}

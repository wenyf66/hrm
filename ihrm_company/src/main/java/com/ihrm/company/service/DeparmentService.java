package com.ihrm.company.service;


import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DeparmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
@Service
public class DeparmentService extends BaseService {

    @Autowired
    private DeparmentDao deparmentDao;

    @Autowired
    private IdWorker idWorker;

    public void add(Department department){
        String id = idWorker.nextId() +"";
        department.setId(id);
        deparmentDao.save(department);
    }

    public void update(Department department){
        Department dept = deparmentDao.findById(department.getId()).get();
        dept.setCode(department.getCode());
        dept.setName(department.getName());
        deparmentDao.save(dept);
    }

    public Department findById(String id){
        return deparmentDao.findById(id).get();
    }

    public List<Department> findAll(String companyId){
        /*Specification<Department> spec = new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };*/
       return  deparmentDao.findAll(getSpec(companyId));
    }

    public void deleteById(String id){
        deparmentDao.deleteById(id);
    }
}

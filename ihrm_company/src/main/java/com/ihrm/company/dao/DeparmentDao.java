package com.ihrm.company.dao;

import com.ihrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
public interface DeparmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
}

package com.ihrm.system.dao;

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    User findByMobile(String mobile);
}

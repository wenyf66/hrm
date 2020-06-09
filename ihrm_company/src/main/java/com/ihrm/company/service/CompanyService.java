package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
@Service
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    public Company findById(String id){
        return companyDao.findById(id).get();
    }

    public List<Company> findAll(){
        return companyDao.findAll();
    }

    public void add(Company company){
        String id = idWorker.nextId()+"";
        company.setId(id);
        company.setName(company.getName());
        companyDao.save(company);
    }

    public void update(Company company){
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        companyDao.save(temp);
    }

    public void deleteById(String id){
        companyDao.deleteById(id);
    }

}

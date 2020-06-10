package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result save(@RequestBody Company company){
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody Company company){
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("id") String id){
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id){
        Company company = companyService.findById(id);
        return new Result(ResultCode.SUCCESS,company);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result findAll(){
        List<Company> list = companyService.findAll();
        return new Result(ResultCode.SUCCESS,list);
    }
}

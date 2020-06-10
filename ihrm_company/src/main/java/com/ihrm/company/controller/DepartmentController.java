package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DeparmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
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
public class DepartmentController extends BaseController {

    @Autowired
    private DeparmentService deparmentService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department){
        department.setCompanyId(companyId);
        deparmentService.add(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findAll(){
        Company company = companyService.findById(companyId);
        List<Department> list = deparmentService.findAll(companyId);
        DeptListResult deptListResult = new DeptListResult(company,list);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }

    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        Department department = deparmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    @RequestMapping(value="/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id,@RequestBody Department department) {
        //1.设置修改的部门id
        department.setId(id);
        //2.调用service更新
        deparmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        deparmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

}

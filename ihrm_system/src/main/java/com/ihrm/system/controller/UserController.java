package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.User;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author WenYingFei
 * @Date 2020/06/10
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;


    /**
     * 保存
     * @return
     */
    @RequestMapping(value = "/user" , method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        //设置保存的用户id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //调用service完成保存用户
        userService.save(user);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据Id查询
     */
    @RequestMapping(value = "/user/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        //添加roleIds(用户已经具有的角色id数组)
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS , user);
    }

    @RequestMapping(value = "/user/{id}" , method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id , @RequestBody User user){
        //调用Service更新
        user.setId(id);
        userService.update( user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据Id删除
     */
    @RequestMapping(value = "/user/{id}" , method = RequestMethod.DELETE , name = "API-USER-DELETE")
    public Result delete(@PathVariable(value = "id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/user" , method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam() Map map){

        //获取当前的企业id
        map.put("companyId" , companyId);
        Page<User> pageUser = userService.findAll(map, page, size);
        //构造返回结果
        PageResult<User> pageResult = new PageResult<>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS , pageResult);
    }


}

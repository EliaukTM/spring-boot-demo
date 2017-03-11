package com.xiamu.controller;

import com.xiamu.bean.Response;
import com.xiamu.entity.tables.pojos.User;
import com.xiamu.service.UserService;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description:
 *
 * @author haoyuan.yang
 * @version 1.0
 * @date: 2017/3/11
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/xiamu")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    @ApiOperation(value = "获取用户", notes = "获取所有的用户的信息")
    public Response<List<User>> getAllUser() {
        return Response.ok(userService.getAllUser());
    }

}

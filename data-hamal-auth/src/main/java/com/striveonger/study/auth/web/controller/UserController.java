package com.striveonger.study.auth.web.controller;

import com.striveonger.study.auth.entity.Users;
import com.striveonger.study.auth.service.IUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:16
 */
@Api(tags = "用户模块")
@RestController
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);


    @Resource
    private IUsersService usersService;


    @ApiOperation(value = "Hello World")
    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam(value = "name") String name) {
        String hello = "Hi: " + name;
        log.info(hello);
        return ResponseEntity.ok(hello);
    }


    @GetMapping("/save")
    public void save() {
        Users user = new Users();
        user.setId("1");
        user.setUsername("xxx");
        user.setPassword("aaa");
        user.setNickname("bbb");
        usersService.save(user);
    }




}
package com.striveonger.study.auth.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.striveonger.study.auth.entity.Users;
import com.striveonger.study.auth.service.IUsersService;
import com.striveonger.study.auth.web.vo.UserRegisterVo;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:16
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUsersService usersService;

    @Resource
    private PasswordEncoder encoder;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(UserRegisterVo vo) {
        synchronized (vo.toString().intern()) {
            // 1. 验证email格式 TODO: 后面再说吧～

            // 2. 检查用户名和邮箱是否已占用
            LambdaQueryWrapper<Users> wrapper = new QueryWrapper<Users>().lambda()
                    .eq(Users::getUsername, vo.getUsername())
                    .or()
                    .eq(Users::getEmail, vo.getEmail());
            long count = usersService.count(wrapper);
            if (count > 0) throw new CustomException(ResultStatus.FAIL, "注册用户失败");
            // 3. 落库
            Users user = new Users();
            user.setUsername(vo.getUsername());
            // 密码加密存储
            user.setPassword(encoder.encode(vo.getPassword()));
            user.setEmail(vo.getEmail());
            user.setStatus(1); // 默认停用状态
            usersService.save(user);
            // 4. 给用户邮箱发送激活邮件 TODO: 后面再说吧～
        }

        return Result.success();
    }

    /**
     * 激活用户 TODO: 激活链接会发送到用户邮箱
     */
    @GetMapping("/activate")
    public void activate() {

    }

}
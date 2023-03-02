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
import java.util.Objects;

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
    public Result<Object> register(UserRegisterVo vo) {
        synchronized (vo.toString().intern()) {
            // 1. 检查用户名和邮箱是否已占用
            LambdaQueryWrapper<Users> wrapper = new QueryWrapper<Users>().lambda()
                    .eq(Users::getUsername, vo.getUsername())
                    .or()
                    .eq(Users::getEmail, vo.getEmail());
            long count = usersService.count(wrapper);
            if (count > 0) throw new CustomException(ResultStatus.FAIL, "注册用户失败");
            // 2. 落库
            Users user = new Users();
            user.setUsername(vo.getUsername());
            // 3. 密码加密存储
            user.setPassword(encoder.encode(vo.getPassword()));
            user.setEmail(vo.getEmail());
            user.setStatus(1); // 默认停用状态
            usersService.save(user);
        }
        return Result.success().message("用户注册成功");
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Object> login(String username, String password) {
        Users hold = usersService.getOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, username));
        if (hold == null) return Result.status(ResultStatus.NOT_FOUND).message("用户名错误");
        String encode = encoder.encode(password);
        if (encode.equals(hold.getPassword())) {
            return Result.success().message("登录成功");
        }
        return Result.fail().message("密码错误");
    }

}
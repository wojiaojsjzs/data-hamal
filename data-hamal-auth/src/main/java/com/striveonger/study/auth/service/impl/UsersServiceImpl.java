package com.striveonger.study.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.striveonger.study.auth.entity.Users;
import com.striveonger.study.auth.mapper.UsersMapper;
import com.striveonger.study.auth.service.IUsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-10-30
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}

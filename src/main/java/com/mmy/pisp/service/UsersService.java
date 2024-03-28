package com.mmy.pisp.service;

import com.mmy.pisp.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface UsersService extends IService<Users> {

    Users findByEmail(String email);

    Users findByName(String name);

    Users findByPhone(String phone);

    int changePassword(Users user);

    int register(Users user);

    int edit(Users user);

}

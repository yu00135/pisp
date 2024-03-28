package com.mmy.pisp.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmy.pisp.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.mapper.UsersMapper;
import com.mmy.pisp.service.UsersService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    private final UsersMapper usersMapper;

    @Autowired
    public UsersServiceImpl(UsersMapper usersMapper){
        this.usersMapper=usersMapper;
    }

    @Override
    public Users findByEmail(String email){
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("user_email", email));
    }

    @Override
    public Users findByName(String name){
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("user_name", name));
    }

    @Override
    public Users findByPhone(String phone){
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("user_phone", phone));
    }

    @Override
    public int changePassword(Users user){
        return usersMapper.updateById(user);
    }

    @Override
    public int register(Users user){
        return usersMapper.insert(user);
    }

    @Override
    public int edit(Users user) {
        return usersMapper.updateById(user);
    }
}

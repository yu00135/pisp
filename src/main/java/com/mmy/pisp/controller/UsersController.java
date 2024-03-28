package com.mmy.pisp.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import com.mmy.pisp.entity.Users;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.service.UsersService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "User API接口文档")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/{name}")
    public Users getUser(@PathVariable String name) {
        return usersService.findByName(name);
    }

    @ApiOperation(value = "密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "登录类型", dataType = "String"),
            @ApiImplicitParam(name = "device", value = "登录设备", dataType = "String")
    })
    @PostMapping("/passwordLogin")
    public ResultVO passwordLogin(@RequestBody Map<String, Object> map) {
        String type = (String) map.get("type");
        String device = (String) map.get("device");
        String password = (String) map.get("password");
        Users user = new Users();
        switch (type) {
            case "email":
                String email = (String) map.get("email");
                user = usersService.findByEmail(email);
                break;
            case "userName":
                String userName = (String) map.get("userName");
                user = usersService.findByName(userName);
                break;
            case "phone":
                String phone = (String) map.get("phone");
                user = usersService.findByPhone(phone);
                break;
            default:
                break;
        }
        if (user != null) {
            if (BCrypt.checkpw(password, user.getUserPassword())) {
                StpUtil.login(user.getUserId(), device);
                StpUtil.getSession().set("user", user);
                return ResultVO.success(200, "登录成功", user);
            } else {
                return ResultVO.fail(331, "密码错误!", null);
            }
        } else {
            return ResultVO.fail(401, "用户不存在！", null);
        }
    }

    @ApiOperation(value = "验证码登录")
    @PostMapping("/verifyLogin")
    public ResultVO verifyLogin(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String type = (String) map.get("type");
        String device = (String) map.get("device");
        String verify = (String) map.get("verify");
        Users user = new Users();
        if ("email".equals(type)) {
            String email = (String) map.get("email");
            user = usersService.findByEmail(email);
            if (user != null) {
                String emailCode = (String) request.getSession().getAttribute("emailCode");
                if (verify.equals(emailCode)) {
                    StpUtil.login(user.getUserId(), device);
                    StpUtil.getSession().set("user", user);
                    request.getSession().setAttribute("emailCode", null);
                } else {
                    return ResultVO.fail(331, "密码错误!", null);
                }
            } else {
                return ResultVO.fail(401, "用户不存在！", null);
            }
        } else if ("phone".equals(type)) {
            String phone = (String) map.get("phone");
            user = usersService.findByPhone(phone);
            if (user != null) {
                String phoneCode = (String) request.getSession().getAttribute("phoneCode");
                if (verify.equals(phoneCode)) {
                    StpUtil.login(user.getUserId(), device);
                    StpUtil.getSession().set("user", user);
                    request.getSession().setAttribute("phoneCode", null);
                } else {
                    return ResultVO.fail(331, "密码错误!", null);
                }
            } else {
                return ResultVO.fail(401, "用户不存在！", null);
            }
        }
        return ResultVO.success(200, "登录成功", user);
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    public ResultVO logout(@RequestBody Map<String, Object> map) {
        String device = (String) map.get("device");
        Integer userId = StpUtil.getLoginIdAsInt();
        StpUtil.logout(userId, device);
        return ResultVO.success(200, "注销成功", null);
    }

    @ApiOperation(value = "检查邮箱,用户名,手机号的唯一性")
    @PostMapping("/check")
    public ResultVO check(@RequestBody Map<String, Object> map) {
        String type = (String) map.get("type");
        String content = (String) map.get("content");
        Users user = new Users();
        switch (type) {
            case "email":
                user = usersService.findByEmail(content);
                break;
            case "userName":
                user = usersService.findByName(content);
                break;
            case "phone":
                user = usersService.findByPhone(content);
                break;
            default:
                break;
        }
        if (user != null) {
            return ResultVO.success(200, "已被注册", type);
        }
        return ResultVO.success(200, "可用", null);
    }

    @ApiOperation(value = "邮箱注册")
    @PostMapping("/emailRegister")
    public ResultVO emailRegister(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String email = (String) map.get("email");
        String userName = (String) map.get("userName");
        String password = BCrypt.hashpw((String) map.get("password"));
        String verify = (String) map.get("verify");
        Users user = new Users();
        user.setUserEmail(email);
        user.setUserName(userName);
        user.setUserPassword(password);
        String emailCode = (String) request.getSession().getAttribute("emailCode");
        if (verify.equals(emailCode)) {
            if (usersService.register(user) > 0) {
                request.getSession().setAttribute("emailCode", null);
                return ResultVO.success(200, "注册成功", null);
            } else {
                return ResultVO.fail(102, "注册失败", null);
            }
        } else {
            return ResultVO.fail(101, "验证码错误", null);
        }
    }

    @PostMapping("/phoneRegister")
    public ResultVO phoneRegister(@RequestBody Map<String, Object> map) {
        String phone = (String) map.get("phone");
        return ResultVO.success(200, "注册成功", null);
    }

    @ApiOperation(value = "更改密码")
    @PostMapping("/changePassword")
    public ResultVO changePassword(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String type = (String) map.get("type");
        String device = (String) map.get("device");
        String email = (String) map.get("email");
        String password = BCrypt.hashpw((String) map.get("password"));
        String verify = (String) map.get("verify");
        if ("email".equals(type)) {
            Users user = usersService.findByEmail(email);
            user.setUserPassword(password);
            String emailCode = (String) request.getSession().getAttribute("emailCode");
            if (verify.equals(emailCode)) {
                if (usersService.changePassword(user) > 0) {
                    if (StpUtil.getLoginId(user.getUserId()) != null) {
                        StpUtil.kickout(user.getUserId(), device);
                    }
                    request.getSession().setAttribute("emailCode", null);
                    return ResultVO.success(200, "更改密码成功", null);
                } else {
                    return ResultVO.fail(102, "更改密码失败", null);
                }
            } else {
                return ResultVO.fail(101, "验证码错误", null);
            }
        } else if ("phone".equals(type)) {
            return ResultVO.fail(100, "该功能施工中", null);
        }
        return ResultVO.fail(102, "更改密码失败", null);
    }


    @Value("${file.uploadPath}")
    private String uploadPath;

    @ApiOperation(value = "上传头像")
    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file") MultipartFile file) {
        Integer userId = StpUtil.getLoginIdAsInt();
        String originalName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalName);
        String path = uploadPath + "avater/" + userId + ".jpg";
        File file1 = new File(path);
        if (!file1.getParentFile().exists()) {
            if(!file1.getParentFile().mkdirs()){
                return ResultVO.fail(500,"创建文件夹失败，无权限",null);
            }
        }
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userProfile="http://127.0.0.1/avater/"+userId+".jpg";
        Users user=usersService.getById(userId);
        user.setUserProfile(userProfile);
        usersService.edit(user);
        return ResultVO.success(200, "头像上传成功", null);
    }

    @PostMapping("/edit")
    public ResultVO edit(@RequestBody Map<String, Object> map) {
        Integer userId=StpUtil.getLoginIdAsInt();
        String userName=(String) map.get("userName");
        String userPhone=(String) map.get("userPhone");
        String userEmail=(String) map.get("userEmail");
        Users user=usersService.getById(userId);
        if(!StringUtil.isNullOrEmpty(userName)){
            user.setUserName(userName);
        }
        if(!StringUtil.isNullOrEmpty(userPhone)){
            user.setUserPhone(userPhone);
        }
        if(!StringUtil.isNullOrEmpty(userEmail)){
            user.setUserEmail(userEmail);
        }
        if(usersService.edit(user)>0){
            return ResultVO.success(200, "修改成功", user);
        }else {
            return ResultVO.fail(500,"修改失败",null);
        }
    }

}

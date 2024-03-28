package com.mmy.pisp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Getter
@Setter
@TableName("users")
@ApiModel(value = "Users对象", description = "")
public class Users implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField("user_name")
    private String userName;

    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 11, message = "密码长度必须是6-16个字符")
    @TableField("user_password")
    private String userPassword;

    @Email(message = "邮箱格式不正确")
    @TableField("user_email")
    private String userEmail;

    @TableField("user_phone")
    private String userPhone;

    @TableField("user_profile")
    private String userProfile;

}

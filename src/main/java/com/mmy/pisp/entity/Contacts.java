package com.mmy.pisp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

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
@TableName("contacts")
@ApiModel(value = "Contacts对象", description = "")
public class Contacts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "contact_id", type = IdType.AUTO)
    private Integer contactId;

    @TableField("contact_name")
    private String contactName;

    @TableField("contact_gender")
    private String contactGender;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("contact_email")
    private String contactEmail;

    @TableField("contact_address")
    private String contactAddress;

    @TableField("user_id")
    private Integer userId;


}

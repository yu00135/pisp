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
@TableName("sites")
@ApiModel(value = "Sites对象", description = "")
public class Sites implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "site_id", type = IdType.AUTO)
    private Integer siteId;

    @TableField("site_name")
    private String siteName;

    @TableField("site_url")
    private String siteUrl;

    @TableField("site_description")
    private String siteDescription;

    @TableField("site_account")
    private String siteAccount;

    @TableField("site_password")
    private String sitePassword;

    @TableField("user_id")
    private Integer userId;

}

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
@TableName("cards")
@ApiModel(value = "Cards对象", description = "")
public class Cards implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "card_id", type = IdType.AUTO)
    private Integer cardId;

    @TableField("card_number")
    private String cardNumber;

    @TableField("card_password")
    private String cardPassword;

    @TableField("card_name")
    private String cardName;

    @TableField("user_id")
    private Integer userId;


}

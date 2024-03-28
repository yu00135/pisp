package com.mmy.pisp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("notes")
@ApiModel(value = "Notes对象", description = "")
public class Notes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "note_id", type = IdType.AUTO)
    private String noteId;

    @TableField("note_title")
    private String noteTitle;

    @TableField("note_content")
    private String noteContent;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("note_date")
    private LocalDateTime noteDate;

    @TableField("note_type")
    private String noteType;

    @TableField("note_tag")
    private String noteTag;

    @TableField("user_id")
    private Integer userId;


}

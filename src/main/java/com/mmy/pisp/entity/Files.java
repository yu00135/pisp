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
@TableName("files")
@ApiModel(value = "Files对象", description = "")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;

    @TableField("file_type")
    private String fileType;

    @TableField("file_name")
    private String fileName;

    @TableField("file_uuid")
    private String fileUuid;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_md5")
    private String fileMd5;

    @TableField("file_count")
    private Integer fileCount;

    @TableField("file_time")
    private String fileTime;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("user_id")
    private Integer userId;

}

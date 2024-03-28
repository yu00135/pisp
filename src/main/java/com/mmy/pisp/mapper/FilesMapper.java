package com.mmy.pisp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.mmy.pisp.entity.Files;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Mapper
public interface FilesMapper extends BaseMapper<Files> {

    List<Files> getChildList(@Param("paramName") String paramName,@Param("fileId") Integer fileId,@Param("userId") Integer userId);

}

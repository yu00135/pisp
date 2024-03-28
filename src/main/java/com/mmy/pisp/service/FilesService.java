package com.mmy.pisp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmy.pisp.entity.Files;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface FilesService extends IService<Files> {

    int addFile(Files file);

    int editFile(Files file);

    int deleteFile(Integer fileId);

    List<Files> getFiles(Integer parentId);

    List<Files> getFolders(Integer parentId);

    List<Files> getChildList(String paramName,Integer fileId);

    int getParentId(Integer fileId);

    List<Files> getPath(Integer fileId);

    Files getFileByMd5(String md5);

    List<Files> getFilesByUuid(String uuid);

    Files getFileById(Integer fileId);

    Files getFileByName(String fileName,Integer parentId);

    List<Files> searchFiles(String fileName);

}

package com.mmy.pisp.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.entity.Files;
import com.mmy.pisp.mapper.FilesMapper;
import com.mmy.pisp.service.FilesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {

    private final FilesMapper filesMapper;

    @Autowired
    public FilesServiceImpl(FilesMapper filesMapper){
        this.filesMapper=filesMapper;
    }

    @Override
    public int addFile(Files file){
        return filesMapper.insert(file);
    }

    @Override
    public int editFile(Files file) {
        return filesMapper.updateById(file);
    }

    @Override
    public int deleteFile(Integer fileId) {
        return filesMapper.deleteById(fileId);
    }


    @Override
    public List<Files> getFiles(Integer parentId){
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectList(new QueryWrapper<Files>().eq("user_id",userId).eq("parent_id",parentId).orderByAsc("file_type"));
    }

    @Override
    public List<Files> getFolders(Integer parentId) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectList(new QueryWrapper<Files>().eq("user_id",userId).eq("parent_id",parentId).eq("file_type","folder"));
    }

    @Override
    public List<Files> getChildList(String paramName,Integer fileId) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.getChildList(paramName,fileId,userId);
    }

    @Override
    public int getParentId(Integer fileId) {
        Files file=filesMapper.selectById(fileId);
        if(file!=null){
            return file.getParentId();
        }else {
            return -1;
        }
    }

    @Override
    public List<Files> getPath(Integer fileId) {
        List<Files> list = new ArrayList<>();
        Files file;
        if(fileId==0){
            return list;
        }
        else{
            file=filesMapper.selectOne(new QueryWrapper<Files>().eq("file_id",fileId));
            list.add(file);
            while(true){
                Integer parentId=file.getParentId();
                if(parentId==0){
                    Collections.reverse(list);
                    return list;
                }else {
                    file=filesMapper.selectOne(new QueryWrapper<Files>().eq("file_id",parentId));
                    list.add(file);
                }
            }
        }
    }

    @Override
    public Files getFileByMd5(String md5) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectOne(new QueryWrapper<Files>().eq("user_id",userId).eq("file_md5",md5));
    }

    @Override
    public List<Files> getFilesByUuid(String uuid) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectList(new QueryWrapper<Files>().eq("user_id",userId).eq("file_uuid",uuid));
    }

    @Override
    public Files getFileById(Integer fileId) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectOne(new QueryWrapper<Files>().eq("user_id",userId).eq("file_id",fileId));
    }

    @Override
    public Files getFileByName(String fileName,Integer parentId) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return filesMapper.selectOne(new QueryWrapper<Files>().eq("user_id",userId).eq("file_name",fileName).eq("parent_id",parentId));
    }

    @Override
    public List<Files> searchFiles(String fileName) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Files> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        if(!StringUtil.isNullOrEmpty(fileName)){
            qw.like("file_name",fileName);
        }
        return filesMapper.selectList(qw);
    }


}


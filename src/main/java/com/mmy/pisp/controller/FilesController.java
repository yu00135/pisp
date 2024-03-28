package com.mmy.pisp.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.mmy.pisp.utils.UUIDUtil;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mmy.pisp.entity.Files;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.result.Space;
import com.mmy.pisp.service.FilesService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Api(tags = "File API接口文档")
@RestController
@RequestMapping("/file")
public class FilesController {

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Value("${file.temp}")
    private String temp;

    private final FilesService filesService;

    @Autowired
    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @ApiOperation(value = "获取文件列表")
    @PostMapping("/getList")
    public ResultVO getList(@RequestBody Map<String, Object> map) {
        Integer parentId = Integer.parseInt(map.get("parentId").toString());
        List<Files> files = filesService.getFiles(parentId);
        return ResultVO.success(200, "获取文件列表成功", files);
    }

    @ApiOperation(value = "获取文件夹列表")
    @PostMapping("/getFolderList")
    public ResultVO getFolderList(@RequestBody Map<String, Object> map) {
        Integer parentId = Integer.parseInt(map.get("parentId").toString());
        List<Files> files = filesService.getFolders(parentId);
        return ResultVO.success(200, "获取文件夹列表成功", files);
    }


    @ApiOperation(value = "获取父id")
    @PostMapping("/getParentId")
    public ResultVO getParentId(@RequestBody Map<String, Object> map) {
        Integer fileId = Integer.parseInt(map.get("parentId").toString());
        Integer parentId = filesService.getParentId(fileId);
        return ResultVO.success(200, "获取父id成功", parentId);
    }

    @PostMapping("/getPath")
    public ResultVO getPath(@RequestBody Map<String, Object> map) {
        Integer fileId = Integer.parseInt(map.get("parentId").toString());
        List<Files> files = filesService.getPath(fileId);
        return ResultVO.success(200, "获取路径成功", files);
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> map) {
        Space space=getSpace();
        if(space.getRemaining()+file.getSize()>space.getTotal()){
            return ResultVO.fail(501,"剩余空间不足，无法上传",null);
        }
        try {
            Integer userId = StpUtil.getLoginIdAsInt();
            Integer parentId = Integer.parseInt(map.get("parentId").toString());
            String originalName = file.getOriginalFilename();
            String suffix = getExtensionName(originalName);
            Files same = filesService.getFileByName(originalName, parentId);
            if (same != null) {
                return ResultVO.fail(501, "上传目录中包含重名文件,请更改文件名", null);
            }
            long size = file.getSize();
            String uuid = UUIDUtil.getUUID();
            byte[] uploadBytes = file.getBytes();
            String md5Hex = DigestUtils.md5DigestAsHex(uploadBytes);
            Files data = filesService.getFileByMd5(md5Hex);
            Files files = new Files();
            if (data == null) {
                files.setParentId(parentId);
                files.setFileMd5(md5Hex);
                files.setFileCount(1);
                files.setFileName(originalName);
                files.setFileUuid(uuid);
                files.setFileType(suffix);
                files.setFileSize(size);
                files.setUserId(userId);
                String path = uploadPath + userId + "/" +uuid + "." +suffix;
                File file1 = new File(path);
                if (!file1.getParentFile().exists()) {
                    file1.getParentFile().mkdirs();
                }
                file.transferTo(file1);
            } else {
                files.setParentId(parentId);
                files.setFileMd5(md5Hex);
                files.setFileCount(1);
                files.setFileName(originalName);
                files.setFileUuid(data.getFileUuid());
                files.setFileType(suffix);
                files.setFileSize(size);
                files.setUserId(userId);
            }
            filesService.addFile(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVO.success(200, "文件上传成功", null);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") Integer fileId, HttpServletResponse response) {
        Integer userId = StpUtil.getLoginIdAsInt();
        Files file = filesService.getFileById(fileId);
        String fileName = file.getFileName();
        String fileType = file.getFileType();
        String uuid = file.getFileUuid();
        String path = uploadPath + userId + "/" + uuid + "." + fileType;
        OutputStream os = null;
        InputStream is = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/x-download;charset=utf-8");
            //Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，
            // 保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
            //把文件名按UTF-8取出，并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            //读取流
            File f = new File(path);
            is = new FileInputStream(f);
            if (is == null) {
                System.out.println("下载附件失败，请检查文件“" + fileName + "”是否存在");
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("下载失败");
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/createFolder")
    public ResultVO createFolder(@RequestBody Map<String, Object> map) {
        Integer userId = StpUtil.getLoginIdAsInt();
        Integer parentId = Integer.parseInt(map.get("parentId").toString());
        String fileName = (String) map.get("fileName");
        Files same = filesService.getFileByName(fileName, parentId);
        if (same != null) {
            return ResultVO.fail(500, "文件名重复,请更改文件名", null);
        }
        Files file = new Files();
        file.setFileType("folder");
        file.setFileName(fileName);
        file.setParentId(parentId);
        file.setUserId(userId);
        filesService.addFile(file);
        return ResultVO.success(200, "新建文件夹成功", null);
    }

    @PostMapping("/rename")
    public ResultVO rename(@RequestBody Map<String, Object> map) {
        Integer fileId = Integer.parseInt(map.get("fileId").toString());
        Integer parentId = Integer.parseInt(map.get("parentId").toString());
        String fileName = (String) map.get("fileName");
        String fileType = (String) map.get("fileType");
        Files same = filesService.getFileByName(fileName, parentId);
        if (same != null) {
            return ResultVO.fail(500, "文件名重复,请更改文件名", null);
        }
        Files file = filesService.getFileById(fileId);
        if (!"folder".equals(fileType)) {
            String afterType = getExtensionName(fileName);
            if (!"folder".equals(afterType)) {
                file.setFileType(afterType);
            }else{
                file.setFileType(null);
            }
        }
        file.setFileName(fileName);
        file.setFileTime(LocalDateTime.now().toString());
        filesService.editFile(file);
        return ResultVO.success(200, "重命名成功", null);
    }


    @PostMapping("/deleteSelected")
    public ResultVO deleteSelected(@RequestBody List<Integer> list) {
        if (list.isEmpty()) {
            return ResultVO.fail(400, "请先选择要删除的文件", null);
        }
        for (Integer i : list) {
            if (filesService.deleteFile(i) < 0) {
                return ResultVO.fail(500, "删除失败", null);
            }
        }
        return ResultVO.success(200, "删除成功", null);
    }


    @PostMapping("/delete")
    public ResultVO delete(@RequestBody Map<String, Object> map) {
        Integer fileId = Integer.parseInt(map.get("fileId").toString());
        Files file = filesService.getFileById(fileId);
        if ("folder".equals(file.getFileType())){
            List<Files> childList = filesService.getChildList("file_id", fileId);
            for (Files f : childList) {
                if("folder".equals(f.getFileType())){
                    filesService.deleteFile(f.getFileId());
                }else{
                    List<Files> list=filesService.getFilesByUuid(file.getFileUuid());
                    if(list.size() == 1){
                        String path = uploadPath + file.getUserId() + "/" + file.getFileUuid() + "." + file.getFileType();
                        File data = new File(path);
                        if (data.exists()) {
                            if (data.delete()) {
                                filesService.deleteFile(fileId);
                                return ResultVO.success(200, "删除成功", null);
                            }
                            return ResultVO.fail(500,"删除失败",null);
                        }
                    }
                    filesService.deleteFile(fileId);
                    return ResultVO.success(200, "删除成功", null);
                }
            }
            return ResultVO.success(200, "删除成功", null);
        } else {
            List<Files> list=filesService.getFilesByUuid(file.getFileUuid());
            if(list.size() == 1){
                String path = uploadPath + file.getUserId() + "/" + file.getFileUuid() + "." + file.getFileType();
                File data = new File(path);
                if (data.exists()) {
                    if (data.delete()) {
                        filesService.deleteFile(fileId);
                        return ResultVO.success(200, "删除成功", null);
                    }
                    return ResultVO.fail(500,"删除失败",null);
                }
            }
            filesService.deleteFile(fileId);
            return ResultVO.success(200, "删除成功", null);
        }
    }

    @PostMapping("/move")
    public ResultVO move(@RequestBody Map<String, Object> map) {
        Integer toId = Integer.parseInt(map.get("toId").toString());
        List<Integer> list=(List) map.get("multipleSelection");
        for(Integer i:list){
            List<Files> pathList=filesService.getPath(toId);
            for(Files f:pathList){
                if(i.equals(f.getFileId())){
                    return ResultVO.fail(500,"不能将文件移动到自身或其子目录下",null);
                }
            }
            Files file=filesService.getFileById(i);
            file.setParentId(toId);
            filesService.editFile(file);
        }
        return ResultVO.success(200, "文件移动成功", null);
    }

    @PostMapping("/copy")   //只能复制非文件夹的文件
    public ResultVO copy(@RequestBody Map<String, Object> map) {
        Integer toId = Integer.parseInt(map.get("toId").toString());
        List<Integer> list=(List) map.get("multipleSelection");
        for(Integer i:list){
            List<Files> pathList=filesService.getPath(toId);
            for(Files f:pathList){
                if(i.equals(f.getFileId())){
                    return ResultVO.fail(500,"不能将文件复制到自身或其子目录下",null);
                }
            }
            Files file=filesService.getFileById(i);
            if("folder".equals(file.getFileType())){
                return ResultVO.fail(500,"暂时不支持复制文件夹",null);
            }else{
                Files newFile=file;
                newFile.setFileId(null);
                newFile.setFileTime(LocalDateTime.now().toString());
                newFile.setParentId(toId);
                filesService.addFile(newFile);
            }
        }

        return ResultVO.success(200, "文件复制成功", null);
    }

    @PostMapping("/search")
    public ResultVO searchFiles(@RequestBody Map<String, Object> map){
        String fileName=(String) map.get("fileName");
        Integer parentId=Integer.parseInt(map.get("parentId").toString());
        List<Files> files=null;
        if(StringUtil.isNullOrEmpty(fileName)){
            System.out.println(parentId);
            files=filesService.getFiles(parentId);
        }else {
            files=filesService.searchFiles(fileName);
        }
        return ResultVO.success(200,"文件查找成功",files);
    }

    public static long folderLength(File folder){
        long length=0;
        //获取文件夹的内的所有文件
        File[] files = folder.listFiles();
        //遍历所有文件
        if (files != null) {
            for (File file : files) {
                //如果当前是文件就相加文件长度
                if (file.isFile()) {
                    length+=file.length();
                }else {
                    //如果当前是文件夹就递归调方法遍历
                    length+=folderLength(file);
                }
            }
        }
        return length;
    }

    public Space getSpace(){
        Integer userId=StpUtil.getLoginIdAsInt();
        String path = uploadPath + userId;
        long length = folderLength(new File(path));
        Space space=new Space();
        Long total=1024L * 1024  * 1024 * 1;
        Long remaining=total-length;
        space.setTotal(total);
        space.setOccupation(length);
        space.setRemaining(remaining);
        return space;
    }

    @PostMapping("/space")
    public ResultVO space() {
        Space space=getSpace();
        return ResultVO.success(200,null,space);
    }

}

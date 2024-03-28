package com.mmy.pisp.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmy.pisp.entity.Notes;
import com.mmy.pisp.utils.UUIDUtil;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.service.NotesService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Api(tags = "Note API接口文档")
@RestController
@RequestMapping("/note")
public class NotesController {

    @Value("${file.uploadPath}")
    private String uploadPath;

    private final NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
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

    @PostMapping("/getNoteList")
    public ResultVO getNoteList() {
        List<Notes> notes = notesService.getNotesByUserId("note");
        return ResultVO.success(200, "获取笔记列表成功", notes);
    }

    @PostMapping("/getDiaryList")
    public ResultVO getDiaryList(@RequestBody Map<String,Object> map) {
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Notes> notes = notesService.getNotesPageByUserId("diary",currentPage,pageSize);
        return ResultVO.success(200, "获取日记列表成功", notes);
    }

    @PostMapping("/getNoteByNoteId")
    public ResultVO getNoteByNoteId(@RequestBody Map<String, Object> map) {
        String noteId=map.get("noteId").toString();
        Notes note = notesService.getNoteByNoteId(noteId);
        return ResultVO.success(200, "获取笔记成功", note);
    }


    @PostMapping("/uploadImage")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> res = new HashMap<>();
        Integer userId = StpUtil.getLoginIdAsInt();
        String originName = file.getOriginalFilename();
        String suffix = getExtensionName(originName);
        String uuid = UUIDUtil.getUUID();
        String path = uploadPath + userId + "/note/" + uuid + "." + suffix;
        try {
            File file1 = new File(path);
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String location="http://127.0.0.1:80/"+userId+"/note/"+ uuid + "." + suffix;
        res.put("location",location);
        return res;
    }

    @PostMapping("/uploadMedia")
    public Map<String, Object> uploadMedia(@RequestParam("file") MultipartFile file){
        Map<String, Object> res = new HashMap<>();
        Integer userId = StpUtil.getLoginIdAsInt();
        String originName = file.getOriginalFilename();
        String suffix = getExtensionName(originName);
        String uuid = UUIDUtil.getUUID();
        String path = uploadPath + userId + "/note/" + uuid + "." + suffix;
        try {
            File file1 = new File(path);
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String location="http://127.0.0.1:80/"+userId+"/note/"+ uuid + "." + suffix;
        res.put("location",location);
        return res;
    }

    @PostMapping("/write")
    public ResultVO write(@RequestBody Map<String, Object> map) {
        String noteType = (String) map.get("noteType");
        Snowflake snowflake = new Snowflake();
        Long noteId = snowflake.nextId();
        String noteTitle = (String) map.get("noteTitle");
        String noteContent = (String) map.get("noteContent");
        LocalDateTime noteDate=null;
        if("diary".equals(noteType)){
            String date = (String) map.get("noteDate");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            noteDate = LocalDateTime.parse(date,dateTimeFormatter);
        }else{
            noteDate=LocalDateTime.now();
        }
        Integer userId = StpUtil.getLoginIdAsInt();
        List<String> tags = (List) map.get("noteTag");
        String noteTag = StringUtils.join(tags, ",");
        Notes note = new Notes();
        note.setNoteId(noteId.toString());
        note.setNoteTitle(noteTitle);
        note.setNoteContent(noteContent);
        note.setNoteDate(noteDate);
        note.setNoteType(noteType);
        note.setNoteTag(noteTag);
        note.setUserId(userId);
        notesService.addNote(note);
//        String str = "aaa,bbb,ccc";
//        String[] strArray = str.split(",");
//        for(String s : strArray){
//            System.out.println(s);
//        }
        cleanCache();
        return ResultVO.success(200, "提交成功", null);
    }

    @PostMapping("/edit")
    public ResultVO edit(@RequestBody Map<String, Object> map) {
        String noteType = (String) map.get("noteType");
        String noteId=map.get("noteId").toString();
        String noteTitle = (String) map.get("noteTitle");
        String noteContent = (String) map.get("noteContent");
        LocalDateTime noteDate=null;
        if("diary".equals(noteType)){
            String date = (String) map.get("noteDate");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            noteDate = LocalDateTime.parse(date,dateTimeFormatter);
        }else{
            noteDate=LocalDateTime.now();
        }
        List<String> tags = (List) map.get("noteTag");
        String noteTag = StringUtils.join(tags, ",");
        Notes note = new Notes();
        note.setNoteId(noteId);
        note.setNoteTitle(noteTitle);
        note.setNoteContent(noteContent);
        note.setNoteDate(noteDate);
        note.setNoteType(noteType);
        note.setNoteTag(noteTag);
        notesService.editNote(note);
        cleanCache();
        return ResultVO.success(200, "修改成功", null);
    }

    @PostMapping("/searchNote")
    public ResultVO searchNote(@RequestBody Map<String, Object> map) {
        String noteTag = (String) map.get("noteTag");
        String noteTitle = (String) map.get("noteTitle");
        String noteContent = (String) map.get("noteContent");
        String noteDate = (String) map.get("noteDate");
        List<Notes> notes = notesService.searchNotes(noteDate,noteTitle,noteContent,noteTag);
        return ResultVO.success(200, "查找成功", notes);
    }

    @PostMapping("/searchDiary")
    public ResultVO searchDiary(@RequestBody Map<String, Object> map) {
        String noteTag = (String) map.get("noteTag");
        String noteTitle = (String) map.get("noteTitle");
        String noteContent = (String) map.get("noteContent");
        String startTime=(String) map.get("start");
        String endTime=(String) map.get("end");
        LocalDateTime start=null;
        LocalDateTime end=null;
        if(!StringUtil.isNullOrEmpty(startTime)&&!StringUtil.isNullOrEmpty(endTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            start=LocalDateTime.parse(startTime,dateTimeFormatter);
            end=LocalDateTime.parse(endTime,dateTimeFormatter);
        }
        List<Notes> notes= notesService.searchDiaries(start,end,noteTitle,noteContent,noteTag);
        return ResultVO.success(200, "查找成功", notes);
    }

    @PostMapping("/delete")
    public ResultVO deleteNote(@RequestBody Map<String, Object> map){
        String noteId=(String) map.get("noteId");
        if(notesService.deleteNote(noteId)>0){
            return ResultVO.success(200, "删除成功", null);
        }else {
            return ResultVO.fail(500,"删除失败",null);
        }
    }

    @PostMapping("/deleteSelected")
    public ResultVO deleteSelected(@RequestBody List<String> list){
        if(list.isEmpty()){
            return ResultVO.fail(400,"请先选择要删除的日记",null);
        }
        for(String i:list){
            if(notesService.deleteNote(i)<0){
                return ResultVO.fail(500,"删除日记失败",null);
            }
        }
        return ResultVO.success(200,"删除日记成功",null);
    }

    @PostMapping("/clean")
    public ResultVO clean(){
        Integer userId=StpUtil.getLoginIdAsInt();
        String folderPath=uploadPath + userId + "/note/";
        File directory = new File(folderPath);
        File [] files = directory.listFiles();
        List<String> list=new ArrayList<>();
        List<String> deleteList=new ArrayList<>();
        if (files != null && files.length != 0) {
            for (File f : files) {
                list.add(f.getName());
            }
        }
        for(String fileName:list){
            List<Notes> notes=notesService.searchNotesByNoteContent(fileName);
            if(notes==null||notes.size()==0){
                deleteList.add(fileName);
            }
        }
        for(String fileName:deleteList){
            String path = uploadPath + userId + "/note/" + fileName;
            File file = new File(path);
            file.delete();
        }
        return ResultVO.success(200,"清理缓存成功",null);
    }

    private void cleanCache(){
        Integer userId=StpUtil.getLoginIdAsInt();
        String folderPath=uploadPath + userId + "/note/";
        File directory = new File(folderPath);
        File [] files = directory.listFiles();
        List<String> list=new ArrayList<>();
        List<String> deleteList=new ArrayList<>();
        if (files != null && files.length != 0) {
            for (File f : files) {
                list.add(f.getName());
            }
        }
        for(String fileName:list){
            List<Notes> notes=notesService.searchNotesByNoteContent(fileName);
            if(notes==null||notes.size()==0){
                deleteList.add(fileName);
            }
        }
        for(String fileName:deleteList){
            String path = uploadPath + userId + "/note/" + fileName;
            File file = new File(path);
            file.delete();
        }
    }

    @PostMapping("/getTagList")
    public ResultVO tagList(@RequestBody Map<String, Object> map){
        String noteType=map.get("noteType").toString();
        TreeSet<String> treeSet= new TreeSet<>();
        List<Notes> list=notesService.getNotesByUserId(noteType);
        for(Notes n:list){
            String str = n.getNoteTag();
            String[] strArray = str.split(",");
            treeSet.addAll(Arrays.asList(strArray));
        }
        treeSet.pollFirst();
        return ResultVO.success(200,"获取标签列表成功",treeSet);
    }

    @PostMapping("/searchNoteByTag")
    public ResultVO searchNoteByTag(@RequestBody HashMap<String,Object> map){
        List<String> list=(List)map.get("selectedTag");
        List<Notes> list1=notesService.searchNotesByTagList(list,"note");
        return ResultVO.success(200,"查询成功",list1);
    }

    @PostMapping("/searchDiaryByTag")
    public ResultVO searchDiaryByTag(@RequestBody HashMap<String,Object> map){
        List<String> list=(List)map.get("selectedTag");
        List<Notes> list1=notesService.searchNotesByTagList(list,"diary");
        return ResultVO.success(200,"查询成功",list1);
    }


}

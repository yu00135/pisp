package com.mmy.pisp.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmy.pisp.entity.Notes;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.mapper.NotesMapper;
import com.mmy.pisp.service.NotesService;

import java.time.LocalDateTime;
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
public class NotesServiceImpl extends ServiceImpl<NotesMapper, Notes> implements NotesService {

    private final NotesMapper notesMapper;

    @Autowired
    public NotesServiceImpl(NotesMapper notesMapper){
        this.notesMapper=notesMapper;
    }

    @Override
    public List<Notes> getNotesByUserId(String noteType) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return notesMapper.selectList(new QueryWrapper<Notes>().eq("user_id",userId).eq("note_type",noteType).orderByDesc("note_date"));
    }

    @Override
    public List<Notes> getNotesByNoteTag(String noteTag) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return notesMapper.selectList(new QueryWrapper<Notes>().eq("user_id",userId).eq("note_tag",noteTag));
    }

    @Override
    public Page<Notes> getNotesPageByUserId(String noteType, int currentPage, int pageSize) {
        Integer userId= StpUtil.getLoginIdAsInt();
        Page<Notes> page=new Page<>(currentPage,pageSize);
        notesMapper.selectPage(page,new QueryWrapper<Notes>().eq("user_id",userId).eq("note_type",noteType).orderByDesc("note_date"));
        return page;
    }

    @Override
    public Notes getNoteByNoteId(String noteId) {
        Integer userId= StpUtil.getLoginIdAsInt();
        return notesMapper.selectOne(new QueryWrapper<Notes>().eq("user_id",userId).eq("note_id",noteId));
    }

    @Override
    public int addNote(Notes note) {
        return notesMapper.insert(note);
    }

    @Override
    public int editNote(Notes note) {
        return notesMapper.updateById(note);
    }

    @Override
    public int deleteNote(String noteId) {
        return notesMapper.deleteById(noteId);
    }

    @Override
    public List<Notes> searchNotes(String noteDate,String noteTitle,String noteContent,String noteTag) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Notes> qw=new QueryWrapper<>();
        qw.eq("user_id",userId).eq("note_type","note");
        if(!StringUtil.isNullOrEmpty(noteDate)){
            qw.like("note_date",noteDate);
        }
        if(!StringUtil.isNullOrEmpty(noteTitle)){
            qw.like("note_title",noteTitle);
        }
        if(!StringUtil.isNullOrEmpty(noteContent)){
            qw.apply("note_content regexp"+"'>[^<]*"+noteContent+"[^>]*<'");

        }
        if(!StringUtil.isNullOrEmpty(noteTag)){
            qw.like("note_tag",noteTag);
        }
        return notesMapper.selectList(qw);
    }

    @Override
    public List<Notes> searchDiaries(LocalDateTime start, LocalDateTime end,String noteTitle,String noteContent,String noteTag) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Notes> qw=new QueryWrapper<>();
        qw.eq("user_id",userId).eq("note_type","diary");
        if(start!=null&&end!=null){
            qw.between("note_date",start,end);
        }
        if(!StringUtil.isNullOrEmpty(noteTitle)){
            qw.like("note_title",noteTitle);
        }
        if(!StringUtil.isNullOrEmpty(noteContent)){
            qw.apply("note_content regexp"+"'>[^<]*"+noteContent+"[^>]*<'");
        }
        if(!StringUtil.isNullOrEmpty(noteTag)){
            qw.like("note_tag",noteTag);
        }
        qw.orderByDesc("note_date");
        return notesMapper.selectList(qw);
    }


    @Override
    public List<Notes> searchNotesByNoteContent(String noteContent) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Notes> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        if(!StringUtil.isNullOrEmpty(noteContent)){
            qw.like("note_content",noteContent);
        }
        return notesMapper.selectList(qw);
    }

    @Override
    public List<Notes> searchNotesByTagList(List<String> list,String noteType){
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Notes> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        qw.eq("note_type",noteType);
        for(String i:list){
            qw.like("note_tag",i);
        }
        return notesMapper.selectList(qw);
    }

}

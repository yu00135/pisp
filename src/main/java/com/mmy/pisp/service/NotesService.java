package com.mmy.pisp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmy.pisp.entity.Notes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface NotesService extends IService<Notes> {

    List<Notes> getNotesByUserId(String noteType);

    List<Notes> getNotesByNoteTag(String noteTag);

    Page<Notes> getNotesPageByUserId(String noteType,int currentPage, int pageSize);

    Notes getNoteByNoteId(String noteId);

    int addNote(Notes note);

    int editNote(Notes note);

    int deleteNote(String noteId);

    List<Notes> searchNotes(String noteDate,String noteTitle,String noteContent,String noteTag);

    List<Notes> searchDiaries(LocalDateTime start, LocalDateTime end,String noteTitle,String noteContent,String noteTag);

    List<Notes> searchNotesByNoteContent(String noteContent);

    List<Notes> searchNotesByTagList(List<String> list,String noteType);
}

package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.Diary;

import java.util.List;

public interface DiaryService {

    List<Diary> getDiaryByUsername(String username);

    void addDiary(Diary diary);

    void addMessage(Integer id, String message);

    void deleteDiaryById(Integer id);

}

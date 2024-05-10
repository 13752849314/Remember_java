package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.entity.Diary;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.DiaryMapper;
import com.happygh0st.remember.service.DiaryService;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryMapper diaryMapper;
    private final UserUtils userUtils;

    public DiaryServiceImpl(DiaryMapper diaryMapper, UserUtils userUtils) {
        this.diaryMapper = diaryMapper;
        this.userUtils = userUtils;
    }

    @Override
    public List<Diary> getDiaryByUsername(String username) {
        return diaryMapper.getDiaryByUsername(username);
    }

    @Override
    public void addDiary(Diary diary) {
        User user = userUtils.getUser();
        diary.setUsername(user.getUsername());
        Date now = userUtils.getLocalTime();
        diary.setCreated_at(now);
        diary.setUpdated_at(now);
        diaryMapper.insert(diary);
    }

    @Override
    public void addMessage(Integer id, String message) {
        User user = userUtils.getUser();
        Diary diary = diaryMapper.selectById(id);
        if (!user.getUsername().equals(diary.getUsername())) {
            throw new RuntimeException("没有权限");
        }
        Date now = userUtils.getLocalTime();
        String format = JwtUtils.TimeFormat.format(now);
        message = "\n#" + format + "\n" + message;
        diary.setMessage(diary.getMessage() + message);
        diary.setUpdated_at(userUtils.getLocalTime());
        diaryMapper.updateById(diary);
    }

    @Override
    public void deleteDiaryById(Integer id) {
        User user = userUtils.getUser();
        Diary diary = diaryMapper.selectById(id);
        if (!user.getUsername().equals(diary.getUsername())) {
            throw new RuntimeException("没有权限");
        }
        diary.setDeleted_at(userUtils.getLocalTime());
        diaryMapper.updateById(diary);
    }
}

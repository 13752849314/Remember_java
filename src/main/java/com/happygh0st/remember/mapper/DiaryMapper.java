package com.happygh0st.remember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happygh0st.remember.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {
    List<Diary> getDiaryByUsername(String username);
}

package com.happygh0st.remember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happygh0st.remember.entity.SystemMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SystemMessageMapper extends BaseMapper<SystemMessage> {

    List<SystemMessage> getPublicMessages();

}

package com.happygh0st.remember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happygh0st.remember.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

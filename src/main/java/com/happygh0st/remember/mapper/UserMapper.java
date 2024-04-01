package com.happygh0st.remember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happygh0st.remember.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> getAllUser();
    User getUserByUsername(String username);
}

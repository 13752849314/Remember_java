package com.happygh0st.remember.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happygh0st.remember.entity.Bill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {

    List<Bill> getBillsByUsername(String username);

}

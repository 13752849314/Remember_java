package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.common.Modifiable;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.Bill;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.BillMapper;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.service.BillService;
import com.happygh0st.remember.utils.UserUtil;
import com.happygh0st.remember.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillMapper billMapper;
    private final UserUtils userUtils;
    private final UserMapper userMapper;
    private final UserUtil userUtil;

    public BillServiceImpl(BillMapper billMapper, UserUtils userUtils, UserMapper userMapper, UserUtil userUtil) {
        this.billMapper = billMapper;
        this.userUtils = userUtils;
        this.userMapper = userMapper;
        this.userUtil = userUtil;
    }

    @Override
    public List<Bill> getBillsByUsername(String username) {
        return billMapper.getBillsByUsername(username);
    }

    @Override
    public void addBill(Bill bill) {
        Date now = userUtils.getLocalTime();
        bill.setCreated_at(now);
        bill.setUpdated_at(now);
        billMapper.insert(bill);
        log.info("账单添加：{}", bill);
    }

    @Override
    public void deleteBillById(Integer id) {
        Bill bill = billMapper.getBillById(id);
        User user = userUtils.getUser();
        if (userUtil.isDo(user, bill)) {
            bill.setDeleted_at(userUtils.getLocalTime());
            billMapper.updateById(bill);
            log.info("账单删除：{}", bill);
        } else {
            throw new RuntimeException("没有权限删除");
        }

    }

    @Override
    public void changeBillInfoById(Integer id, Map<String, String> map) {
        Bill bill = billMapper.getBillById(id);
        User user = userUtils.getUser();
        try {
            if (!user.getUsername().equals(bill.getUsername())) { //不是修改自己的账单
                // 拿到账单主人
                User billUser = userMapper.getUserByUsername(bill.getUsername());
                // 权限判断
                if (!Role.ge(user.getRoles(), billUser.getRoles())) {
                    throw new RuntimeException("没有权限修改");
                }
            }
            userUtils.changeFields(bill, map, Modifiable.class);
            bill.setUpdated_at(userUtils.getLocalTime());
            billMapper.updateById(bill);
            log.info("修改信息为：{}", map);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Bill> getAllBills() {
        return billMapper.selectList(null);
    }
}

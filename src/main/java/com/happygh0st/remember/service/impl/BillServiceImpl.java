package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.common.Modifiable;
import com.happygh0st.remember.entity.Bill;
import com.happygh0st.remember.mapper.BillMapper;
import com.happygh0st.remember.service.BillService;
import com.happygh0st.remember.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillMapper billMapper;
    private final UserUtils userUtils;

    public BillServiceImpl(BillMapper billMapper, UserUtils userUtils) {
        this.billMapper = billMapper;
        this.userUtils = userUtils;
    }

    @Override
    public List<Bill> getBillsByUsername(String username) {
        return billMapper.getBillsByUsername(username);
    }

    @Override
    public void addBill(Bill bill) {
        LocalDateTime now = LocalDateTime.now();
        bill.setCreated_at(now);
        bill.setUpdated_at(now);
        billMapper.insert(bill);
    }

    @Override
    public void deleteBillById(Integer id) {
        Bill bill = billMapper.getBillById(id);
        bill.setDeleted_at(LocalDateTime.now());
        billMapper.updateById(bill);
    }

    @Override
    public void changeBillInfoById(Integer id, Map<String, String> map) {
        Bill bill = billMapper.getBillById(id);
        try {
            userUtils.changeFields(bill, map, Modifiable.class);
            bill.setUpdated_at(userUtils.getLocalTime());
            billMapper.updateById(bill);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Bill> getAllBills() {
        return billMapper.selectList(null);
    }
}

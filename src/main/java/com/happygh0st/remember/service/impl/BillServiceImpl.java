package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.entity.Bill;
import com.happygh0st.remember.mapper.BillMapper;
import com.happygh0st.remember.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillMapper billMapper;

    public BillServiceImpl(BillMapper billMapper) {
        this.billMapper = billMapper;
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
}

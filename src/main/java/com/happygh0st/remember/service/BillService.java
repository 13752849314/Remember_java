package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {

    List<Bill> getBillsByUsername(String username);

    void addBill(Bill bill);

    void deleteBillById(Integer id);

    void changeBillInfoById(Integer id, Map<String,String> map);

}

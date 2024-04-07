package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.Bill;

import java.util.List;

public interface BillService {

    List<Bill> getBillsByUsername(String username);

    void addBill(Bill bill);

}

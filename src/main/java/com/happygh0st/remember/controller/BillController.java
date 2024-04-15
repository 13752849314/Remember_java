package com.happygh0st.remember.controller;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.Bill;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.service.BillService;
import com.happygh0st.remember.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {

    private final BillService billService;
    private final UserUtils userUtils;

    public BillController(BillService billService, UserUtils userUtils) {
        this.billService = billService;
        this.userUtils = userUtils;
    }

    @PostMapping("/add")
    @Roles()
    public Results addBill(@RequestBody Bill bill) {
        HttpServletRequest request = userUtils.getRequest();
        User user = (User) request.getAttribute("user");
        bill.setUsername(user.getUsername());
        billService.addBill(bill);
        return Results.StatusOk().setMessage("添加成功");
    }

    @GetMapping("/bill")
    @Roles()
    public Results getBillsByUsername() {
        HttpServletRequest request = userUtils.getRequest();
        User user = (User) request.getAttribute("user");
        List<Bill> bills = billService.getBillsByUsername(user.getUsername());
        return Results.StatusOk().setMessage("获取成功").addData("bills", bills);
    }

    @PostMapping("/delete/{id}")
    @Roles()
    public Results deleteBillById(@PathVariable("id") Integer id) {
        try {
            billService.deleteBillById(id);
            return Results.StatusOk().setMessage("删除成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/changeBI/{id}")
    @Roles()
    public Results changeBillInfoById(@PathVariable("id") Integer id, @RequestBody Map<String, String> map) {
        try {
            billService.changeBillInfoById(id, map);
            return Results.StatusOk().setMessage("信息修改成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @GetMapping("/bills")
    @Roles(Role.ADMINS)
    public Results getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return Results.StatusOk().setMessage("获取成功").addData("bills", bills);
    }
}

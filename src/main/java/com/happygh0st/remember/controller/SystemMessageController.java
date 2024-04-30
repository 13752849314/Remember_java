package com.happygh0st.remember.controller;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.SystemMessage;
import com.happygh0st.remember.service.SystemMessageService;
import com.happygh0st.remember.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/sys")
public class SystemMessageController {

    private final SystemMessageService systemMessageService;

    public SystemMessageController(SystemMessageService systemMessageService) {
        this.systemMessageService = systemMessageService;
    }

    @GetMapping("/list")
    @Roles()
    public Results systemMessageList() {
        try {
            List<SystemMessage> messages = systemMessageService.getAllSystemMessages();
            return Results.StatusOk().setMessage("获取成功").addData("systemMessages", messages);
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/add")
    @Roles(value = Role.ADMIN)
    public Results addMessage(@RequestBody SystemMessage message) {
        try {
            systemMessageService.addSystemMessage(message);
            return Results.StatusOk().setMessage("添加成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/publish/{id}")
    @Roles(value = Role.ADMIN)
    public Results publishMessage(@PathVariable Integer id, @RequestBody Map<String, Object> data) {
        try {
            String times = (String) data.getOrDefault("time", null);
            Date time;
            if (Objects.isNull(times)) {
                time = null;
            } else {
                time = JwtUtils.TimeFormat.parse(times);
            }
            systemMessageService.publishMessage(id, time);
            return Results.StatusOk().setMessage("发布成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/ap")
    @Roles(value = Role.ADMIN)
    public Results addAndPublishMessage(@RequestBody SystemMessage message) {
        try {
            systemMessageService.addAndPublishMessage(message);
            return Results.StatusOk().setMessage("添加并发布成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/deleted/{id}")
    @Roles(Role.ADMIN)
    public Results deletedMessageBuId(@PathVariable Integer id) {
        try {
            systemMessageService.deleteMessageById(id);
            return Results.StatusOk().setMessage("删除成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/change/{id}")
    @Roles(Role.ADMIN)
    public Results changeMessageById(@PathVariable Integer id, @RequestBody Map<String,String> data) {
        try {
            systemMessageService.changeMessageById(id, data.getOrDefault("message",""));
            return Results.StatusOk().setMessage("修改成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

}

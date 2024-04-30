package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.SystemMessage;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.SystemMessageMapper;
import com.happygh0st.remember.service.SystemMessageService;
import com.happygh0st.remember.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {

    private final SystemMessageMapper systemMessageMapper;
    private final UserUtils userUtils;

    public SystemMessageServiceImpl(SystemMessageMapper systemMessageMapper, UserUtils userUtils) {
        this.systemMessageMapper = systemMessageMapper;
        this.userUtils = userUtils;
    }

    @Override
    public List<SystemMessage> getAllSystemMessages() {
        User user = userUtils.getUser();
        if (Role.valueOf(user.getRoles()).equals(Role.ADMINS)) {
            return systemMessageMapper.selectList(null);
        }
        return systemMessageMapper.getPublicMessages();
    }

    @Override
    public void addSystemMessage(SystemMessage message) {
        Date time = userUtils.getLocalTime();
        message.setCreated_at(time);
        message.setUpdated_at(time);
        systemMessageMapper.insert(message);
    }

    @Override
    public void publishMessage(Integer id, Date publish_time) {
        SystemMessage message = systemMessageMapper.selectById(id);
        message.setPublish_time(publish_time);
        systemMessageMapper.updateById(message);
    }

    @Override
    public void addAndPublishMessage(SystemMessage message) {
        Date time = userUtils.getLocalTime();
        message.setCreated_at(time);
        message.setUpdated_at(time);
        message.setPublish_time(time);
        systemMessageMapper.insert(message);
    }

    @Override
    public void deleteMessageById(Integer id) {
        SystemMessage message = systemMessageMapper.selectById(id);
        message.setDeleted_at(userUtils.getLocalTime());
        systemMessageMapper.updateById(message);
    }

    @Override
    public void changeMessageById(Integer id, String message) {
        User user = userUtils.getUser();
        SystemMessage m = systemMessageMapper.selectById(id);
        if (Role.valueOf(user.getRoles()).equals(Role.ADMINS) || user.getUsername().equals(m.getPublisher())) {
            m.setMessage(message);
            systemMessageMapper.updateById(m);
        } else {
            throw new RuntimeException("权限不够");
        }
    }
}

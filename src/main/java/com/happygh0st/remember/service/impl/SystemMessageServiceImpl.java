package com.happygh0st.remember.service.impl;

import cn.hutool.core.util.StrUtil;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.SystemMessage;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.SystemMessageMapper;
import com.happygh0st.remember.service.SystemMessageService;
import com.happygh0st.remember.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        if (user.getRoles().equals(Role.ADMINS.getValue())) {
            return systemMessageMapper.selectList(null);
        }
        return systemMessageMapper.getPublicMessages();
    }

    @Override
    public void addSystemMessage(SystemMessage message) {
        User user = userUtils.getUser();
        Date time = userUtils.getLocalTime();
        message.setCreated_at(time);
        message.setUpdated_at(time);
        message.setPublisher(user.getUsername());
        systemMessageMapper.insert(message);
    }

    @Override
    public void publishMessage(Integer id, Date publish_time) {
        SystemMessage message = systemMessageMapper.selectById(id);
        if (Objects.isNull(publish_time)) {
            publish_time = userUtils.getLocalTime();
        }
        message.setPublish_time(publish_time);
        message.setUpdated_at(userUtils.getLocalTime());
        systemMessageMapper.updateById(message);
    }

    @Override
    public void addAndPublishMessage(SystemMessage message) {
        User user = userUtils.getUser();
        Date time = userUtils.getLocalTime();
        message.setCreated_at(time);
        message.setUpdated_at(time);
        message.setPublish_time(time);
        message.setPublisher(user.getUsername());
        systemMessageMapper.insert(message);
    }

    @Override
    public void deleteMessageById(Integer id) {
        User user = userUtils.getUser();
        SystemMessage message = systemMessageMapper.selectById(id);
        if (user.getRoles().equals(Role.ADMINS.getValue()) || user.getUsername().equals(message.getPublisher())) {
            message.setDeleted_at(userUtils.getLocalTime());
            systemMessageMapper.updateById(message);
        } else {
            throw new RuntimeException("权限不够");
        }
    }

    @Override
    public void changeMessageById(Integer id, String message) {
        if (StrUtil.isEmpty(message)) {
            return;
        }
        User user = userUtils.getUser();
        SystemMessage m = systemMessageMapper.selectById(id);
        if (user.getRoles().equals(Role.ADMINS.getValue()) || user.getUsername().equals(m.getPublisher())) {
            m.setMessage(message);
            systemMessageMapper.updateById(m);
        } else {
            throw new RuntimeException("权限不够");
        }
    }
}

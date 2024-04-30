package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.SystemMessage;

import java.util.Date;
import java.util.List;

public interface SystemMessageService {

    List<SystemMessage> getAllSystemMessages();

    void addSystemMessage(SystemMessage message);

    void publishMessage(Integer id, Date publish_time);

    void addAndPublishMessage(SystemMessage message);

    void deleteMessageById(Integer id);

    void changeMessageById(Integer id, String message);

}

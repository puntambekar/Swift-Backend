package com.sportify.swift.service;

import com.sportify.swift.dao.MessageRepository;
import com.sportify.swift.entity.Message;
import com.sportify.swift.requestmodel.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    public void addMessage(MessageRequest messageRequest) {
        Message message = new Message();
        message.setName(messageRequest.getName());
        message.setEmail(messageRequest.getEmail());
        message.setMessage(messageRequest.getMessage());

        messageRepository.save(message);
    }

    public List<Message> getAllMessages() {

       return messageRepository.findAll();
    }
}

package com.sportify.swift.controller;

import com.sportify.swift.entity.Message;
import com.sportify.swift.requestmodel.MessageRequest;
import com.sportify.swift.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<Void> addMessage(@RequestBody MessageRequest messageRequest){
        messageService.addMessage(messageRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<Message> getAllMessages(){
        return  messageService.getAllMessages();

    }

}

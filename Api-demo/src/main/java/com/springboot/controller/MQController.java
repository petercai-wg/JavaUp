package com.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.mq.MQProducerService;

@RestController
@RequestMapping("/api/mq")
public class MQController {
    @Autowired
    private MQProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        producerService.publishText(message);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("MQ Service is running");
    }

}
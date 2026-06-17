package com.example.bidly.domain.chatmessage.controller;

import com.example.bidly.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.bidly.domain.chatmessage.dto.response.ChatMessageResponse;
import com.example.bidly.domain.chatmessage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        ChatMessageResponse response = chatMessageService.sendMessage(request);
        messagingTemplate.convertAndSend("/sub/chat/room" + request.getRoomId(), response);
        return ResponseEntity.ok(response);
    }
}
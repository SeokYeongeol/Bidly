package com.example.bidly.domain.chatroom.controller;

import com.example.bidly.domain.chatroom.dto.response.ChatRoomResponse;
import com.example.bidly.domain.chatroom.service.ChatRoomService;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/v1/chat-rooms")
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @AuthenticationPrincipal Auth auth,
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(auth, productId));
    }

    @GetMapping("/v1/chat-rooms")
    public ResponseEntity<PagedModel<ChatRoomResponse>> getMyChatRooms(
            @AuthenticationPrincipal Auth auth,
            @RequestParam int page
    ) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(auth, page));
    }
}

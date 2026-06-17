package com.example.bidly.domain.chatmessage.dto.response;

import com.example.bidly.domain.chatmessage.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {

    private final Long id;
    private final Long roomId;
    private final Long senderId;
    private final String senderNickname;
    private final String contents;
    private final LocalDateTime createdAt;

    public static ChatMessageResponse of(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .senderNickname(chatMessage.getSender().getName())
                .contents(chatMessage.getContents())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}

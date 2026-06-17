package com.example.bidly.domain.chatmessage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatMessageRequest {

    @NotNull(message = "내용을 입력해주세요.")
    private String contents;

    private Long roomId;
    private Long senderId;
}

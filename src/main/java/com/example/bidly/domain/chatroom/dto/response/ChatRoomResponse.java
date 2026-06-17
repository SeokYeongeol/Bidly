package com.example.bidly.domain.chatroom.dto.response;

import com.example.bidly.domain.chatroom.entity.ChatRoom;
import com.example.bidly.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private final Long id;
    private final Long productId;
    private final String productTitle;
    private final Long opponentId;
    private String opponentNickname;

    public static ChatRoomResponse of(ChatRoom chatRoom, Long myId) {
        boolean isBuyer = chatRoom.getBuyer().getId().equals(myId);
        Member opponent = isBuyer ? chatRoom.getSeller() : chatRoom.getBuyer();

        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .productId(chatRoom.getProduct().getId())
                .productTitle(chatRoom.getProduct().getTitle())
                .opponentId(opponent.getId())
                .opponentNickname(opponent.getName())
                .build();
    }
}

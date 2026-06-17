package com.example.bidly.domain.chatmessage.entity;

import com.example.bidly.domain.chatroom.entity.ChatRoom;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatMessage extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Builder
    public ChatMessage(String contents, ChatRoom chatRoom, Member sender) {
        this.contents = contents;
        this.chatRoom = chatRoom;
        this.sender = sender;
    }
}

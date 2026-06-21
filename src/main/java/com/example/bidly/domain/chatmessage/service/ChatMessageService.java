package com.example.bidly.domain.chatmessage.service;

import com.example.bidly.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.bidly.domain.chatmessage.dto.response.ChatMessageResponse;
import com.example.bidly.domain.chatmessage.entity.ChatMessage;
import com.example.bidly.domain.chatmessage.repository.ChatMessageRepository;
import com.example.bidly.domain.chatroom.entity.ChatRoom;
import com.example.bidly.domain.chatroom.repository.ChatRoomRepository;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.notification.enums.NotificationType;
import com.example.bidly.domain.notification.event.NotificationEvent;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        ChatRoom findChatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ServerException(CHAT_ROOM_NOT_FOUND));

        Member findSender = memberRepository.findMemberById(request.getSenderId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        if (!findChatRoom.isParticipant(findSender.getId())) {
            throw new ServerException(NOT_CHAT_PARTICIPANT);
        }

        ChatMessage savedMessage = ChatMessage.builder()
                .chatRoom(findChatRoom)
                .sender(findSender)
                .contents(request.getContents())
                .build();
        chatMessageRepository.save(savedMessage);

        Long receiverId = findChatRoom.getBuyer().getId().equals(request.getSenderId())
                ? findChatRoom.getSeller().getId()
                : findChatRoom.getBuyer().getId();

        eventPublisher.publishEvent(new NotificationEvent(
                receiverId,
                NotificationType.CHAT_RECEIVED,
                savedMessage.getSender().getName() + " 님이 메시지를 보냈습니다."
        ));
        return ChatMessageResponse.of(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponse::of)
                .toList();
    }
}

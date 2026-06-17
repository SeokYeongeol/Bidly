package com.example.bidly.domain.chatroom.repository;

import com.example.bidly.domain.chatroom.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByBuyerIdOrSellerId(Long buyerId, Long sellerId, Pageable pageable);
    Optional<ChatRoom> findByProductIdAndBuyerId(Long productId, Long buyerId);
}

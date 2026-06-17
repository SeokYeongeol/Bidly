package com.example.bidly.domain.chatroom.service;

import com.example.bidly.domain.chatroom.dto.response.ChatRoomResponse;
import com.example.bidly.domain.chatroom.entity.ChatRoom;
import com.example.bidly.domain.chatroom.repository.ChatRoomRepository;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(Auth auth, Long productId) {
        Member findBuyer = memberRepository.findMemberById(auth.getId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ServerException(PRODUCT_NOT_FOUND));

        if (findProduct.getSeller().getId().equals(findBuyer.getId())) {
            throw new ServerException(CANNOT_CHAT_WITH_SELF);
        }

        ChatRoom findChatRoom = chatRoomRepository.findByProductIdAndBuyerId(productId, findBuyer.getId())
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .product(findProduct)
                                .buyer(findBuyer)
                                .seller(findProduct.getSeller())
                                .build()
                ));
        return ChatRoomResponse.of(findChatRoom, findBuyer.getId());
    }

    @Transactional(readOnly = true)
    public PagedModel<ChatRoomResponse> getMyChatRooms(Auth auth, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ChatRoom> rooms = chatRoomRepository.findByBuyerIdOrSellerId(auth.getId(), auth.getId(), pageable);

        return new PagedModel<>(rooms.map(room -> ChatRoomResponse.of(room, auth.getId())));
    }
}

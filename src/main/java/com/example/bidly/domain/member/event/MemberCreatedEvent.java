package com.example.bidly.domain.member.event;

import com.example.bidly.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberCreatedEvent {

    private final Long memberId;
}

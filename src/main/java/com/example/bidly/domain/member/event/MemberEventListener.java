package com.example.bidly.domain.member.event;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.point.entity.Point;
import com.example.bidly.domain.point.repository.PointRepository;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.example.bidly.global.exception.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMemberCreated(MemberCreatedEvent event) {
        Member findMember = memberRepository.findById(event.getMemberId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        pointRepository.save(Point.builder()
                .member(findMember)
                .point(0L)
                .build());
    }
}

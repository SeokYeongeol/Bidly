package com.example.bidly.domain.point.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.point.dto.request.ChargePointRequest;
import com.example.bidly.domain.point.dto.response.ChargePointResponse;
import com.example.bidly.domain.point.dto.response.PortOnePaymentResponse;
import com.example.bidly.domain.point.entity.Point;
import com.example.bidly.domain.point.entity.PointHistory;
import com.example.bidly.domain.point.entity.PointPayment;
import com.example.bidly.domain.point.enums.PaymentStatus;
import com.example.bidly.domain.point.enums.PointType;
import com.example.bidly.domain.point.repository.PointHistoryRepository;
import com.example.bidly.domain.point.repository.PointPaymentRepository;
import com.example.bidly.domain.point.repository.PointRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import com.example.bidly.global.service.PortOneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PointPaymentService {

    private final PointPaymentRepository pointPaymentRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;
    private final PortOneService portOneService;
    private final MemberRepository memberRepository;

    @Transactional
    public ChargePointResponse portOnePayment(Auth auth, ChargePointRequest request) {
        Member findMember = memberRepository.findMemberById(auth.getId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        pointPaymentRepository.findByPaymentId(request.getPaymentId())
                .ifPresent(exists -> {
                    if (exists.getPaymentStatus() == PaymentStatus.FAILED) {
                        throw new ServerException(ALREADY_PAYMENT_FAILED);
                    }
                    if (exists.getPaymentStatus() == PaymentStatus.SUCCESS) {
                        throw new ServerException(ALREADY_PAYMENT_SUCCESS);
                    }
                });
        PointPayment pointPayment = savePending(findMember, request.getPaymentId());

        try {
            PortOnePaymentResponse payment = portOneService.getPayment(request.getPaymentId());

            if (!"PAID".equals(payment.getStatus())) {
                markFailed(pointPayment.getId());
                throw new ServerException(PAYMENT_NOT_SUCCESS);
            }
            Long amount = payment.getAmount().getTotal();

            return markSuccessAndCharge(pointPayment.getId(), findMember.getId(), amount);
        } catch (ServerException se) {
            throw se;
        } catch (Exception e) {
            markFailed(pointPayment.getId());
            throw new ServerException(PAYMENT_VALID_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PointPayment savePending(Member member, String paymentId) {
        PointPayment payment = PointPayment.builder()
                .paymentId(paymentId)
                .member(member)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        return pointPaymentRepository.save(payment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long paymentId) {
        PointPayment pointPayment = pointPaymentRepository.findById(paymentId)
                .orElseThrow();
        pointPayment.changeStatus(PaymentStatus.FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChargePointResponse markSuccessAndCharge(Long paymentId, Long memberId, Long amount) {
        PointPayment pointPayment = pointPaymentRepository.findById(paymentId)
                .orElseThrow();
        pointPayment.changeStatus(PaymentStatus.SUCCESS);
        pointPayment.changeAmount(amount);

        Point point = pointRepository.findPointsByMemberId(memberId)
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));
        point.chargePoint(amount);

        PointHistory savedPointHistory = PointHistory.builder()
                .amount(amount)
                .point(point)
                .description("포인트 충전")
                .type(PointType.CHARGE)
                .build();
        pointHistoryRepository.save(savedPointHistory);

        return ChargePointResponse.builder()
                .currentBalance(point.getPoint())
                .chargedAmount(amount)
                .status(PaymentStatus.SUCCESS)
                .build();
    }
}

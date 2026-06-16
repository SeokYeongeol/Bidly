package com.example.bidly.domain.point.service;

import com.example.bidly.domain.member.entity.Member;
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
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PointPaymentService {

    private final PointPaymentRepository pointPaymentRepository;
    private final IamportClient iamportClient;
    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;

    @Transactional
    public IamportResponse<Payment> iamportPayment(Auth auth, String imp_uid) {
        // 결제 테이블에 해당 imp_uid 가 있는지 확인 (있는 상태일 때 해당 status 가 SUCCESS, FAILED 이면 예외)
        pointPaymentRepository.findByImpUid(imp_uid)
                .ifPresent(exists -> {
                    if (exists.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                        throw new ServerException(ALREADY_PAYMENT_SUCCESS);
                    }
                    if (exists.getPaymentStatus().equals(PaymentStatus.FAILED)) {
                        throw new ServerException(ALREADY_PAYMENT_FAILED);
                    }
                });

        // 우선 imp_uid 에 unique 제약조건이 걸려있기 때문에 PENDING 상태로 우선 저장과 flush
        Member findMember = Member.fromAuth(auth.getId());
        PointPayment pointPayment = PointPayment.builder()
                .impUid(imp_uid)
                .member(findMember)
                .amount(null)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        pointPaymentRepository.saveAndFlush(pointPayment);

        // 결제 수행
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
            Payment payment = response.getResponse();

            int price = payment.getAmount().intValue();
            String status = payment.getStatus();

            if (!status.equals("paid")) {
                pointPayment.changeStatus(PaymentStatus.FAILED);
                throw new ServerException(PAYMENT_NOT_SUCCESS);
            }
            pointPayment.changeStatus(PaymentStatus.SUCCESS);

            Point findPoint = pointRepository.findPointsByMemberId(findMember.getId())
                    .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

            PointHistory savedPointHistory = PointHistory.builder()
                    .amount(price)
                    .type(PointType.CHARGE)
                    .description("포인트 충전")
                    .point(findPoint)
                    .build();
            pointHistoryRepository.save(savedPointHistory);

            return response;
        } catch (Exception e) {
            pointPayment.changeStatus(PaymentStatus.FAILED);
            pointPaymentRepository.save(pointPayment);
            throw new ServerException(PAYMENT_VALID_ERROR);
        }
    }
}

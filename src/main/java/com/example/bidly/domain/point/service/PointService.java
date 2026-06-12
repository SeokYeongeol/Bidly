package com.example.bidly.domain.point.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.point.dto.request.ExchangePointRequest;
import com.example.bidly.domain.point.dto.response.PointResponse;
import com.example.bidly.domain.point.entity.Point;
import com.example.bidly.domain.point.entity.PointHistory;
import com.example.bidly.domain.point.enums.PointType;
import com.example.bidly.domain.point.repository.PointHistoryRepository;
import com.example.bidly.domain.point.repository.PointRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bidly.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointResponse exchangePoint(Auth auth, ExchangePointRequest request) {
        Point findPoint = pointRepository.findPointsByMemberId(auth.getId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));
        findPoint.chargePoint(request.getPoint());

        PointHistory savedPointHistory = PointHistory.builder()
                .amount(request.getPoint())
                .description("포인트 충전")
                .type(PointType.CHARGE)
                .point(findPoint)
                .build();
        pointRepository.save(findPoint);
        pointHistoryRepository.save(savedPointHistory);

        return PointResponse.of(findPoint);
    }
}

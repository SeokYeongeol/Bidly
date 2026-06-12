package com.example.bidly.domain.point.dto.response;

import com.example.bidly.domain.point.entity.Point;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PointResponse {

    private final Long id;
    private final Long memberId;
    private final Integer point;

    public static PointResponse of(Point point) {
        return PointResponse.builder()
                .id(point.getId())
                .memberId(point.getMember().getId())
                .point(point.getPoint())
                .build();
    }
}
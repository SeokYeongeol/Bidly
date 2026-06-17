package com.example.bidly.domain.point.entity;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.global.entity.TimeStamped;
import com.example.bidly.global.exception.ServerException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.bidly.global.exception.ErrorCode.INSUFFICIENT_POINT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long point;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Point(Long point, Member member) {
        this.point = point;
        this.member = member;
    }

    public void chargePoint(Long point) {
        this.point += point;
    }

    public void usePoint(Long point) {
        if (this.point < point) {
            throw new ServerException(INSUFFICIENT_POINT);
        }
        this.point -= point;
    }
}

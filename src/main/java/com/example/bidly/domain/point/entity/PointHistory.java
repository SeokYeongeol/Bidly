package com.example.bidly.domain.point.entity;

import com.example.bidly.domain.point.enums.PointType;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointHistory extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Enumerated(EnumType.STRING)
    private PointType type;

    @Builder
    public PointHistory(Long amount, String description, Point point, PointType type) {
        this.amount = amount;
        this.description = description;
        this.point = point;
        this.type = type;
    }
}

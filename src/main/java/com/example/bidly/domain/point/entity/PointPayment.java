package com.example.bidly.domain.point.entity;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.point.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String impUid;

    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Builder
    public PointPayment(String impUid, Integer amount, Member member, PaymentStatus paymentStatus) {
        this.impUid = impUid;
        this.amount = amount;
        this.member = member;
        this.paymentStatus = paymentStatus;
    }

    public void changeStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

package com.example.bidly.domain.point.repository;

import com.example.bidly.domain.point.entity.PointPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointPaymentRepository extends JpaRepository<PointPayment, Long> {
    Optional<PointPayment> findByImpUid(String impUid);
}

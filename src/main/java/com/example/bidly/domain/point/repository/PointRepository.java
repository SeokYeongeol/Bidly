package com.example.bidly.domain.point.repository;

import com.example.bidly.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findPointsByMemberId(Long memberId);
}

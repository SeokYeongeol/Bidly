package com.example.bidly.domain.notification.repository;

import com.example.bidly.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.member.id = :memberId and n.isRead is false order by n.createdAt desc")
    List<Notification> findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(@Param("memberId") Long memberId);

    Page<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
}

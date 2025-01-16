package com.homestay.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homestay.management.model.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Tìm kiếm nhận xét theo phòng
    List<Review> findByRoomId(Long roomId);
    // Tìm kiếm nhận xét của khách hàng
    List<Review> findByCustomerName(String customerName);
}
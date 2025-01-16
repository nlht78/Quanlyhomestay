package com.homestay.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.homestay.management.model.Booking;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Tìm kiếm đặt phòng theo tên khách hàng
    List<Booking> findByCustomerName(String customerName);
    // Tìm kiếm đặt phòng theo phòng
    List<Booking> findByRoomId(Long roomId);
    
    List<Booking> findByCustomerEmail(String email);

    
    
 // Tổng doanh thu
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = 'COMPLETED'")
    Double calculateTotalRevenue();

    // Doanh thu theo tháng
    @Query("SELECT MONTH(b.checkInDate), SUM(b.totalPrice) FROM Booking b WHERE b.status = 'Completed' GROUP BY MONTH(b.checkInDate)")
    List<Object[]> calculateMonthlyRevenue();

    // Doanh thu theo loại phòng
    @Query("SELECT r.type, SUM(b.totalPrice) FROM Booking b JOIN b.room r WHERE b.status = 'Completed' GROUP BY r.type")
    List<Object[]> calculateRevenueByRoomType();
    
    //Doanh thu theo từng phòng
    @Query("SELECT b.room.name, SUM(b.totalPrice) " +
    	       "FROM Booking b " +
    	       "WHERE b.status = 'COMPLETED' " +
    	       "GROUP BY b.room.name")
    	List<Object[]> calculateRevenueByRoom();
}

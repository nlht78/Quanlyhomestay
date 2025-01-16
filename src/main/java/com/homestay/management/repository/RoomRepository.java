package com.homestay.management.repository;



import com.homestay.management.model.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh tại đây nếu cần
    Room findByName(String name);
    // Ví dụ: tìm kiếm phòng theo tình trạng có sẵn
    List<Room> findByAvailable(boolean available);
    
    @Query("SELECT r FROM Room r JOIN Booking b ON r.id = b.room.id WHERE b.customer.email = :customerEmail AND b.status = :status")
    List<Room> findRoomsByCustomerEmailAndBookingStatus(@Param("customerEmail") String customerEmail, @Param("status") String status);

    @Query("SELECT r FROM Room r JOIN Booking b ON r.id = b.room.id WHERE b.customer.name = :customerName AND b.status = :status")
    List<Room> findRoomsByCustomerNameAndBookingStatus(@Param("customerName") String customerName, @Param("status") String status);
    
    List<Room> findByType(String type);
    
    List<Room> findByTypeAndAvailable(String type, boolean available);
}
package com.homestay.management.repository;

import com.homestay.management.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByBookingId(Long bookingId);
    
    void deleteByBookingId(Long bookingId); // Tìm và xóa các hóa đơn liên quan đến bookingId

}

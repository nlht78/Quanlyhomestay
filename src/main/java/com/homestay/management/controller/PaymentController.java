package com.homestay.management.controller;

import com.homestay.management.model.Booking;
import com.homestay.management.model.Invoice;
import com.homestay.management.repository.BookingRepository;
import com.homestay.management.repository.InvoiceRepository;
import com.homestay.management.repository.RoomRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class PaymentController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Phương thức hiển thị trang thanh toán (payment)
     */
    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam Long bookingId, Model model) {
        // Lấy thông tin booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Tạo invoice nếu chưa tồn tại
        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseGet(() -> {
                    Invoice newInvoice = new Invoice(booking, booking.getTotalPrice());
                    invoiceRepository.save(newInvoice);
                    return newInvoice;
                });

        // Thêm thông tin vào model
        model.addAttribute("booking", booking);
        model.addAttribute("invoice", invoice);

        return "payment"; // Tên file HTML trang payment
    }

    /**
     * Phương thức xác nhận đặt phòng và chuyển đến trang xác nhận thanh toán
     */
    @PostMapping("/payment/confirm")
    public String confirmBooking(@RequestParam Long bookingId, 
                                 @RequestParam String phone, 
                                 @RequestParam String email,
                                 @RequestParam String specialRequest, 
                                 HttpSession session, 
                                 Model model) {
        // Kiểm tra trạng thái người dùng
        String userName = (String) session.getAttribute("userName");
        if (userName == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập
        }

        // Lấy thông tin booking và invoice
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Cập nhật thông tin liên hệ
        booking.setPhone(phone);
        booking.setEmail(email);
        booking.setSpecialRequest(specialRequest);
        bookingRepository.save(booking);

        // Thêm thông tin vào model
        model.addAttribute("booking", booking);
        model.addAttribute("invoice", invoice);

        return "redirect:/payment/confirmation?bookingId=" + bookingId;
    }

    /**
     * Phương thức hiển thị trang xác nhận thanh toán (payment-confirmation)
     */
    @GetMapping("/payment/confirmation")
    public String showConfirmationPage(@RequestParam Long bookingId, Model model) {
        // Lấy thông tin booking và invoice
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Invoice invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Thêm thông tin vào model
        model.addAttribute("booking", booking);
        model.addAttribute("invoice", invoice);

        return "payment-confirmation"; // Tên file HTML trang xác nhận thanh toán
    }
}

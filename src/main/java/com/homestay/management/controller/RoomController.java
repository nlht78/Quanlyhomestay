package com.homestay.management.controller;

import com.homestay.management.model.Booking;
import com.homestay.management.model.Customer;
import com.homestay.management.model.Review;
import com.homestay.management.model.Room;
import com.homestay.management.repository.BookingRepository;
import com.homestay.management.repository.CustomerRepository;
import com.homestay.management.repository.ReviewRepository;
import com.homestay.management.repository.RoomRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Hiển thị thông tin chi tiết phòng
    @GetMapping("/rooms/{id}")
    public String viewRoomDetails(@PathVariable Long id, Model model) {
        Room room = roomRepository.findById(id).orElse(null);

        if (room == null) {
            model.addAttribute("errorMessage", "Room not found!");
            return "error"; // Trang hiển thị lỗi
        }

        model.addAttribute("room", room);

        // Lấy danh sách đánh giá của phòng
        List<Review> reviews = reviewRepository.findByRoomId(id);
        model.addAttribute("reviews", reviews);

        return "room-details"; // Trang hiển thị chi tiết phòng
    }

    @PostMapping("/rooms/book")
    public String bookRoom(@RequestParam Long roomId,
                           @RequestParam String checkInDate,
                           @RequestParam String checkOutDate,
                           @RequestParam int guests,
                           @RequestParam double totalPrice, // Tổng giá nhận từ form
                           HttpSession session,
                           Model model) {
        // Lấy email từ session
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            return "redirect:/login"; // Chuyển hướng nếu chưa đăng nhập
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Customer customer = customerRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Tạo booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckInDate(LocalDate.parse(checkInDate));
        booking.setCheckOutDate(LocalDate.parse(checkOutDate));
        booking.setGuests(guests);
        booking.setTotalPrice(totalPrice); // Sử dụng tổng giá từ form

        bookingRepository.save(booking);

        model.addAttribute("booking", booking);
        return "redirect:/payment?bookingId=" + booking.getId();
    }

}

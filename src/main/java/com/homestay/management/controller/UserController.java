package com.homestay.management.controller;

import com.homestay.management.model.Booking;
import com.homestay.management.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/user/bookings")
    public String viewUserBookings(HttpSession session, Model model) {
        // Lấy thông tin email từ session
        String userEmail = (String) session.getAttribute("userEmail");

        // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (userEmail == null) {
            return "redirect:/login";
        }

        // Lấy danh sách đặt phòng của người dùng từ database
        List<Booking> userBookings = bookingRepository.findByCustomerEmail(userEmail);
        model.addAttribute("userBookings", userBookings);

        return "bookings"; // Trả về trang hiển thị danh sách đặt phòng
    }
}

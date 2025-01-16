package com.homestay.management.controller;

import com.homestay.management.model.Review;
import com.homestay.management.model.Room;
import com.homestay.management.repository.ReviewRepository;
import com.homestay.management.repository.RoomRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Hiển thị trang đánh giá
     */
    @GetMapping("/review")
    public String showReviewPage(HttpSession session, Model model) {
        // Kiểm tra trạng thái đăng nhập
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login"; // Chuyển hướng nếu chưa đăng nhập
        }

        // Lấy danh sách phòng mà người dùng đã đặt thành công
        List<Room> rooms = roomRepository.findRoomsByCustomerEmailAndBookingStatus(userEmail, "Completed");
        model.addAttribute("rooms", rooms);

        return "review"; // Tên file HTML hiển thị form đánh giá
    }

    /**
     * Xử lý submit đánh giá
     */
    @PostMapping("/review/submit")
    public String submitReview(@RequestParam Long roomId,
                               @RequestParam String customerName,
                               @RequestParam String comment,
                               @RequestParam int rating,
                               @RequestParam String reviewDate) {
        // Lấy phòng đánh giá
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Tạo đối tượng Review và lưu vào cơ sở dữ liệu
        Review review = new Review(customerName, comment, rating, LocalDate.parse(reviewDate), room);
        reviewRepository.save(review);

        return "redirect:/"; // Quay lại trang chủ sau khi gửi đánh giá
    }
}

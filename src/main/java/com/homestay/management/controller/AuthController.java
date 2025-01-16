package com.homestay.management.controller;

import com.homestay.management.model.Customer;
import com.homestay.management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    // Đăng ký người dùng mới
    @PostMapping("/register")
    public String registerUser(@ModelAttribute Customer customer, Model model) {
        // Kiểm tra xem email đã tồn tại hay chưa
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            model.addAttribute("message", "Email đã tồn tại!");
            return "register"; // Quay lại trang đăng ký nếu có lỗi
        }

        // Gán vai trò mặc định là ROLE_USER
        customer.setRole("ROLE_USER");

        // Lưu người dùng mới vào cơ sở dữ liệu
        customerRepository.save(customer);
        model.addAttribute("message", "Đăng ký thành công!");
        return "login"; // Quay lại trang đăng nhập sau khi đăng ký thành công
    }

    // Đăng nhập người dùng
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model,
                            HttpSession session) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(email);

        // Kiểm tra nếu người dùng tồn tại và mật khẩu khớp
        if (existingCustomer.isPresent() && existingCustomer.get().getPassword().equals(password)) {
            Customer customer = existingCustomer.get();
            session.setAttribute("userName", customer.getName());
            session.setAttribute("userEmail", customer.getEmail()); // Lưu email vào session
            session.setAttribute("userRole", customer.getRole()); // Lưu vai trò vào session

            // Kiểm tra vai trò và chuyển hướng
            if ("ROLE_ADMIN".equals(customer.getRole())) {
                return "redirect:/admin"; // Chuyển hướng đến trang admin
            } else {
                return "redirect:/"; // Chuyển hướng đến trang home cho user
            }
        } else {
            model.addAttribute("message", "Email hoặc mật khẩu không đúng!");
            return "login"; // Quay lại trang đăng nhập nếu thông tin không hợp lệ
        }
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Trả về trang đăng ký
    }

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "redirected", required = false) String redirected, Model model) {
    	if ("true".equals(redirected)) {
            model.addAttribute("message", "Bạn cần đăng nhập với tài khoản admin để truy cập.");
        }
        return "login"; // Trả về trang đăng nhập
    }

    // Đăng xuất người dùng
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa tất cả các thông tin lưu trữ trong session
        return "redirect:/"; // Chuyển hướng về trang chủ
    }

   
}

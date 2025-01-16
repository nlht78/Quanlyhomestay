package com.homestay.management.controller;

import com.homestay.management.model.Room;
import com.homestay.management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly,
            @RequestParam(required = false) String sortBy,
            Model model,
            HttpSession session) {

        // Lấy tên người dùng và vai trò từ session
        String userName = (String) session.getAttribute("userName");
        String userRole = (String) session.getAttribute("userRole");

        // Lấy danh sách phòng
        List<Room> rooms;
        if (availableOnly) {
            rooms = (type == null || type.isEmpty()) ?
                    roomService.getAvailableRooms() :
                    roomService.getAvailableRoomsByType(type);
        } else {
            rooms = (type == null || type.isEmpty()) ?
                    roomService.getAllRooms() :
                    roomService.getRoomsByType(type);
        }

        // Sắp xếp danh sách phòng dựa trên giá trị `sortBy`
        if ("asc".equals(sortBy)) {
            rooms.sort((r1, r2) -> Double.compare(r1.getPrice(), r2.getPrice()));
        } else if ("desc".equals(sortBy)) {
            rooms.sort((r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice()));
        }

        // Thêm dữ liệu vào model
        model.addAttribute("rooms", rooms);
        model.addAttribute("type", type);
        model.addAttribute("availableOnly", availableOnly);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", userRole);

        return "home";
    }


}

package com.homestay.management.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Lấy HttpSession
        HttpSession session = request.getSession();

        // Lấy dữ liệu userName và userRole từ session
        String userName = (String) session.getAttribute("userName");
        String userRole = (String) session.getAttribute("userRole");

        // Kiểm tra và thêm dữ liệu vào ModelAndView nếu không null
        if (modelAndView != null) {
            modelAndView.addObject("userName", userName);
            modelAndView.addObject("userRole", userRole);
        }
    }
}

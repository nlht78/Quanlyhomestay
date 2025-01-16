package com.homestay.management.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserSessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Kiểm tra nếu session có thông tin user
        String userName = (String) request.getSession().getAttribute("userName");
        String userRole = (String) request.getSession().getAttribute("userRole");

        // Nếu không có thông tin user, chuyển hướng hoặc xử lý theo yêu cầu
        if (userName == null || userRole == null) {
            // Ví dụ: Chuyển hướng đến trang đăng nhập
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // Gắn userName và userRole vào model để sử dụng trong header
            String userName = (String) request.getSession().getAttribute("userName");
            String userRole = (String) request.getSession().getAttribute("userRole");
            modelAndView.addObject("userName", userName);
            modelAndView.addObject("userRole", userRole);
        }
    }
}

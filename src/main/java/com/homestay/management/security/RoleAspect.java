package com.homestay.management.security;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleAspect {

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;
    
    // Xử lý annotation @AdminOnly
    @Before("@annotation(com.homestay.management.security.AdminOnly)")
    public void checkAdminRole() throws IOException {
        String userRole = (String) session.getAttribute("userRole");
        
        if (userRole == null || !"ROLE_ADMIN".equals(userRole)) {
        	response.sendRedirect("/login?redirected=true");
        }
    }
}

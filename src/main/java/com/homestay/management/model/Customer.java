package com.homestay.management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;  // Mật khẩu
    private String role; // Vai trò: ADMIN hoặc USER
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// Getter và Setter
    public Long getId() {
        return id;
    }
    
// // Tự động gán ROLE_USER trước khi lưu vào database
//    @PrePersist
//    public void setDefaultRole() {
//        if (this.role == null) {
//            this.role = "ROLE_USER";
//        }
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

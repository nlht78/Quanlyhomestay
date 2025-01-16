package com.homestay.management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mã đặt phòng

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // Liên kết với khách hàng
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) // Liên kết với phòng
    private Room room;

    @Column(nullable = false)
    private LocalDate checkInDate; // Ngày nhận phòng

    @Column(nullable = false)
    private LocalDate checkOutDate; // Ngày trả phòng

    @Column(nullable = false)
    private int guests; // Số lượng khách

    @Column(nullable = false)
    private double totalPrice; // Tổng giá phòng

    private String phone; // Số điện thoại liên hệ của khách hàng

    private String email; // Email liên hệ của khách hàng

    @Column(length = 500)
    private String specialRequest; // Yêu cầu đặc biệt của khách hàng

    @Column(nullable = false, length = 20)
    private String status = "Pending"; // Trạng thái đặt phòng (Pending, Confirmed, Completed, Cancelled)

    // Constructor mặc định
    public Booking() {}

    // Constructor đầy đủ
    public Booking(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate, int guests, double totalPrice, String phone, String email) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.totalPrice = totalPrice;
        this.phone = phone;
        this.email = email;
        this.status = "Pending";
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

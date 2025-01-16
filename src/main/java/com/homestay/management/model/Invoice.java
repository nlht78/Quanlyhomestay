package com.homestay.management.model;

import jakarta.persistence.*;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mã hóa đơn

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false) // Liên kết với đặt phòng
    private Booking booking;

    private double paymentAmount; // Số tiền thanh toán

    @Column(nullable = false, length = 20)
    private String paymentStatus = "Unpaid"; // Trạng thái thanh toán (Unpaid, Paid, Failed)

    // Constructor mặc định
    public Invoice() {}

    // Constructor với tham số
    public Invoice(Booking booking, double paymentAmount) {
        this.booking = booking;
        this.paymentAmount = paymentAmount;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

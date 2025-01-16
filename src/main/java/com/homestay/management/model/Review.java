package com.homestay.management.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Mã nhận xét

    private String customerName;  // Tên khách hàng
    private String comment;  // Nhận xét của khách hàng
    private int rating;  // Đánh giá (1-5 sao)
    private LocalDate reviewDate;  // Ngày nhận xét

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;  // Phòng được nhận xét
 
    // Constructors, Getters, Setters
    public Review() {}

    public Review(String customerName, String comment, int rating, LocalDate reviewDate, Room room) {
        this.customerName = customerName;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.room = room;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
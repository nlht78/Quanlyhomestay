package com.homestay.management.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Mã phòng, là khóa chính

    private String name;    // Tên phòng (ví dụ: Phòng A, Phòng B, ...)
    private String type;    // Loại phòng (ví dụ: Single, Double, Suite)
    private double price;   // Giá thuê
    private boolean available; // Trạng thái phòng (Có sẵn/Không có sẵn)
    private String image;   // Đường dẫn đến hình ảnh phòng

    @ElementCollection
    private List<String> additionalImages; // Danh sách ảnh chi tiết
    
 // Các trường mới
    private int numberOfBedrooms;  // Số phòng ngủ
    public int getNumberOfBedrooms() {
		return numberOfBedrooms;
	}

    
    public List<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }
	public void setNumberOfBedrooms(int numberOfBedrooms) {
		this.numberOfBedrooms = numberOfBedrooms;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public int getMaxGuests() {
		return maxGuests;
	}

	public void setMaxGuests(int maxGuests) {
		this.maxGuests = maxGuests;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	private int numberOfBeds;      // Số giường
    private int maxGuests;         // Số lượng khách tối đa
    private String amenities;      // Tiện nghi (danh sách chuỗi phân cách bởi dấu phẩy)
    private String rules;          // Quy định phòng
    
    
    // Constructor mặc định
    public Room() {}

    // Constructor với tham số (để sử dụng khi tạo phòng mới)
    public Room(String name, String type, double price, boolean available, String image) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.available = available;
        this.image = image;  // Nếu có hình ảnh
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.homestay.management.controller;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.homestay.management.model.Booking;
import com.homestay.management.model.Review;
import com.homestay.management.model.Room;
import com.homestay.management.repository.BookingRepository;
import com.homestay.management.repository.CustomerRepository;
import com.homestay.management.repository.InvoiceRepository;
import com.homestay.management.repository.ReviewRepository;
import com.homestay.management.repository.RoomRepository;
import com.homestay.management.security.AdminOnly;

import jakarta.transaction.Transactional;

@Controller
public class AdminController {

	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private RoomRepository roomRepository;
	
	@Autowired
    private BookingRepository bookingRepository;
	
	@Autowired
    private ReviewRepository reviewRepository;
	
	@Autowired
    private InvoiceRepository invoiceRepository;
	
	
	@Value("${file.upload-dir}") // Lấy đường dẫn từ application.properties
    private String uploadDir;

	@AdminOnly
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Thêm các thông tin cần hiển thị trong dashboard admin
        return "admin/dashboard";
    }

    
	@AdminOnly
    @GetMapping("/admin/manage-customers")
    public String manageCustomers(Model model) {
        // Lấy danh sách khách hàng từ cơ sở dữ liệu
        model.addAttribute("customers", customerRepository.findAll());
        return "admin/manage-customers"; // Tên file HTML hiển thị danh sách khách hàng
    }
	
	@AdminOnly
    @PostMapping("/admin/delete-customer")
    public String deleteCustomer(@RequestParam Long customerId) {
        // Xóa khách hàng theo ID
        customerRepository.deleteById(customerId);
        return "redirect:/admin/manage-customers"; // Quay lại danh sách khách hàng sau khi xóa
    }

    /**
     * Quản lý đặt phòng
     */
	@AdminOnly
    @GetMapping("/admin/manage-bookings")
    public String manageBookings(Model model) {
        List<Booking> bookings = bookingRepository.findAll(); // Lấy tất cả các đặt phòng
        model.addAttribute("bookings", bookings); // Gửi danh sách đặt phòng đến view
        return "admin/manage-bookings"; // Trả về view "manage-bookings.html"
    }
	
	@AdminOnly
	@Transactional
	@PostMapping("/admin/delete-booking")
	public String deleteBooking(@RequestParam Long bookingId) {
	    // Xóa tất cả các bản ghi trong bảng invoice liên quan đến bookingId
	    invoiceRepository.deleteByBookingId(bookingId);

	    // Xóa bản ghi trong bảng booking
	    bookingRepository.deleteById(bookingId);

	    return "redirect:/admin/manage-bookings"; // Quay lại danh sách đặt phòng sau khi xóa
	}

    /**
     * Hiển thị danh sách phòng
     */
	@AdminOnly
    @GetMapping("/admin/manage-rooms")
    public String manageRooms(Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        return "admin/manage-rooms";
    }

    /**
     * Xóa phòng
     */
	@AdminOnly
    @PostMapping("/admin/delete-room")
    public String deleteRoom(@RequestParam Long roomId) {
        roomRepository.deleteById(roomId);
        return "redirect:/admin/manage-rooms";
    }

    /**
     * Hiển thị form chỉnh sửa phòng
     */
	@AdminOnly
    @GetMapping("/admin/edit-room/{id}")
    public String editRoomForm(@PathVariable Long id, Model model) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        model.addAttribute("room", room);
        return "admin/edit-room";
    }

    /**
     * Xử lý chỉnh sửa phòng
     */
	@AdminOnly
	@PostMapping("/admin/edit-room")
	public String editRoom(@RequestParam Long id,
	                       @RequestParam String name,
	                       @RequestParam String type,
	                       @RequestParam double price,
	                       @RequestParam int numberOfBedrooms,
	                       @RequestParam int numberOfBeds,
	                       @RequestParam int maxGuests,
	                       @RequestParam String amenities,
	                       @RequestParam String rules,
	                       @RequestParam boolean available, // Nhận trạng thái từ form
	                       @RequestParam MultipartFile imageFile,
	                       @RequestParam MultipartFile[] additionalImagesFiles) throws IOException {

	    Room room = roomRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Room not found"));

	    room.setName(name);
	    room.setType(type);
	    room.setPrice(price);
	    room.setNumberOfBedrooms(numberOfBedrooms);
	    room.setNumberOfBeds(numberOfBeds);
	    room.setMaxGuests(maxGuests);
	    room.setAmenities(amenities);
	    room.setRules(rules);
	    room.setAvailable(available); // Cập nhật trạng thái phòng

	    if (!imageFile.isEmpty()) {
	        String mainImagePath = saveImage(imageFile);
	        room.setImage(mainImagePath);
	    }

	    List<String> additionalImagesPaths = new ArrayList<>();
	    for (MultipartFile additionalImageFile : additionalImagesFiles) {
	        if (!additionalImageFile.isEmpty()) {
	            additionalImagesPaths.add(saveImage(additionalImageFile));
	        }
	    }
	    room.setAdditionalImages(additionalImagesPaths);

	    roomRepository.save(room);
	    return "redirect:/admin/manage-rooms";
	}


 // Hiển thị form thêm phòng
	@AdminOnly
    @GetMapping("/admin/add-room")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room()); // Gửi một đối tượng Room rỗng đến form
        return "admin/add-room";
    }

    // Xử lý lưu phòng mới
	@AdminOnly
    @PostMapping("/admin/add-room")
    public String saveNewRoom(@RequestParam String name,
                              @RequestParam String type,
                              @RequestParam double price,
                              @RequestParam int numberOfBedrooms,
                              @RequestParam int numberOfBeds,
                              @RequestParam int maxGuests,
                              @RequestParam String amenities,
                              @RequestParam String rules,
                              @RequestParam boolean available,
                              @RequestParam MultipartFile imageFile,
                              @RequestParam MultipartFile[] additionalImagesFiles) throws IOException {

        // Tạo đối tượng Room mới
        Room room = new Room();
        room.setName(name);
        room.setType(type);
        room.setPrice(price);
        room.setNumberOfBedrooms(numberOfBedrooms);
        room.setNumberOfBeds(numberOfBeds);
        room.setMaxGuests(maxGuests);
        room.setAmenities(amenities);
        room.setRules(rules);
        room.setAvailable(available);

        // Lưu ảnh chính
        if (!imageFile.isEmpty()) {
            String mainImagePath = saveImage(imageFile);
            room.setImage(mainImagePath);
        }

        // Lưu ảnh chi tiết
        List<String> additionalImagesPaths = new ArrayList<>();
        for (MultipartFile additionalImageFile : additionalImagesFiles) {
            if (!additionalImageFile.isEmpty()) {
                additionalImagesPaths.add(saveImage(additionalImageFile));
            }
        }
        room.setAdditionalImages(additionalImagesPaths);

        // Lưu phòng vào cơ sở dữ liệu
        roomRepository.save(room);

        return "redirect:/admin/manage-rooms";
    }
    
	/**
     * Hiển thị danh sách đánh giá
     */
	@AdminOnly
    @GetMapping("/admin/manage-reviews")
    public String manageReviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "admin/manage-reviews"; // Tên file HTML để hiển thị danh sách đánh giá
    }

    /**
     * Xóa đánh giá
     */
	@AdminOnly
    @PostMapping("/admin/delete-review")
    public String deleteReview(@RequestParam Long reviewId) {
        reviewRepository.deleteById(reviewId); // Xóa đánh giá theo ID
        return "redirect:/admin/manage-reviews"; // Quay lại danh sách đánh giá sau khi xóa
    }
	
	// Hiển thị trang thêm đánh giá
	@AdminOnly
	@GetMapping("/admin/add-review")
	public String addReviewForm(Model model) {
	    model.addAttribute("rooms", roomRepository.findAll()); // Lấy danh sách phòng từ DB
	    return "admin/add-review"; // Tên file HTML cho trang thêm đánh giá
	}

	// Xử lý thêm đánh giá
	@AdminOnly
	@PostMapping("/admin/add-review")
	public String saveReview(
	        @RequestParam String customerName,
	        @RequestParam String comment,
	        @RequestParam int rating,
	        @RequestParam LocalDate reviewDate,
	        @RequestParam Long roomId) {

	    // Lấy thông tin phòng từ roomId
	    Room room = roomRepository.findById(roomId)
	            .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));

	    // Tạo đối tượng Review mới
	    Review review = new Review();
	    review.setCustomerName(customerName);
	    review.setComment(comment);
	    review.setRating(rating);
	    review.setReviewDate(reviewDate);
	    review.setRoom(room);

	    // Lưu đánh giá vào cơ sở dữ liệu
	    reviewRepository.save(review);

	    return "redirect:/admin/manage-reviews"; // Quay lại trang quản lý đánh giá
	}
	
	/**
     * Hiển thị trang báo cáo
     */
	@AdminOnly
	@GetMapping("/admin/reports")
	public String viewReports(Model model) {
	    // Sử dụng DecimalFormat để định dạng số
	    DecimalFormat decimalFormat = new DecimalFormat("#,###");

	    // Tổng doanh thu
	    Double totalRevenue = bookingRepository.calculateTotalRevenue();
	    String formattedTotalRevenue = totalRevenue != null ? decimalFormat.format(totalRevenue) : "0";
	    model.addAttribute("totalRevenue", formattedTotalRevenue);

	    // Doanh thu theo tháng
	    List<Object[]> monthlyRevenue = bookingRepository.calculateMonthlyRevenue();
	    List<String[]> formattedMonthlyRevenue = monthlyRevenue.stream()
	            .map(revenue -> new String[]{
	                    "Tháng " + revenue[0],
	                    decimalFormat.format(revenue[1])
	            })
	            .collect(Collectors.toList());
	    model.addAttribute("monthlyRevenue", formattedMonthlyRevenue);

	    // Doanh thu theo loại phòng
	    List<Object[]> revenueByRoomType = bookingRepository.calculateRevenueByRoomType();
	    List<String[]> formattedRevenueByRoomType = revenueByRoomType.stream()
	            .map(revenue -> new String[]{
	                    revenue[0].toString(),
	                    decimalFormat.format(revenue[1])
	            })
	            .collect(Collectors.toList());
	    model.addAttribute("revenueByRoomType", formattedRevenueByRoomType);

	    // Doanh thu theo từng phòng
	    List<Object[]> revenueByRoom = bookingRepository.calculateRevenueByRoom();
	    List<String[]> formattedRevenueByRoom = revenueByRoom.stream()
	            .map(revenue -> new String[]{
	                    revenue[0].toString(), // Tên phòng
	                    decimalFormat.format(revenue[1]) // Doanh thu
	            })
	            .collect(Collectors.toList());
	    model.addAttribute("revenueByRoom", formattedRevenueByRoom);

	    return "admin/reports";
	}


	
    private String saveImage(MultipartFile file) throws IOException {
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        File destinationFile = new File(directory, fileName);
        file.transferTo(destinationFile);
        return fileName; // Trả về tên file để lưu vào DB
    }
    

    
}

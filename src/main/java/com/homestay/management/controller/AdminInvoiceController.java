package com.homestay.management.controller;

import com.homestay.management.model.Booking;
import com.homestay.management.model.Invoice;
import com.homestay.management.repository.BookingRepository;
import com.homestay.management.repository.InvoiceRepository;
import com.homestay.management.security.AdminOnly;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.internet.MimeMessage;

@Controller
public class AdminInvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Hiển thị danh sách hóa đơn cho admin
     */
    @AdminOnly
    @GetMapping("/admin/invoices")
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceRepository.findAll());
        return "admin/manage-invoices"; // Tên file HTML hiển thị danh sách hóa đơn
    }

    /**
     * Xử lý thay đổi trạng thái hóa đơn
     */
    @AdminOnly
    @PostMapping("/admin/invoice/update-status")
    public String updateInvoiceStatus(@RequestParam Long invoiceId, @RequestParam String status, Model model) throws MessagingException {
        // Lấy thông tin hóa đơn
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Lấy thông tin đặt phòng liên quan
        Booking booking = invoice.getBooking();

        // Cập nhật trạng thái hóa đơn
        invoice.setPaymentStatus(status);
        invoiceRepository.save(invoice);

        // Nếu trạng thái là "Completed", cập nhật trạng thái booking, phòng và gửi email
        if ("Completed".equalsIgnoreCase(status)) {
            // Cập nhật trạng thái booking
            booking.setStatus("Completed");
            bookingRepository.save(booking);

            // Cập nhật trạng thái phòng
            booking.getRoom().setAvailable(false); // Đặt trạng thái phòng thành "Không khả dụng"
            bookingRepository.save(booking); // Lưu lại booking để cập nhật thông tin phòng (cascade)
            
            // Gửi email xác nhận
            sendCompletionEmail(booking);
        }

        return "redirect:/admin/invoices"; // Quay lại danh sách hóa đơn
    }


    /**
     * Gửi email xác nhận hoàn thành hóa đơn và booking
     */
    private void sendCompletionEmail(Booking booking) throws MessagingException {
        String subject = "Xác nhận đặt phòng thành công - Homestay Management";
        String recipientEmail = booking.getEmail();
        String content = generateEmailContent(booking);

        // Tạo email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        // Gửi email
        mailSender.send(message);
    }

    /**
     * Tạo nội dung email
     */
    private String generateEmailContent(Booking booking) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Xin chào ").append(booking.getCustomer().getName()).append(",</h1>");
        content.append("<p>Cảm ơn bạn đã đặt phòng tại hệ thống Homestay của chúng tôi. Dưới đây là thông tin chi tiết:</p>");

        // Thông tin phòng
        content.append("<h2>Thông tin phòng</h2>");
        content.append("<p><strong>Tên phòng:</strong> ").append(booking.getRoom().getName()).append("</p>");
        content.append("<p><strong>Loại phòng:</strong> ").append(booking.getRoom().getType()).append("</p>");
        content.append("<p><strong>Thời gian:</strong> ")
                .append(booking.getCheckInDate()).append(" - ").append(booking.getCheckOutDate()).append("</p>");
        content.append("<p><strong>Số khách:</strong> ").append(booking.getGuests()).append("</p>");

        // Thông tin booking
        content.append("<h2>Thông tin đặt phòng</h2>");
        content.append("<p><strong>Mã đặt phòng:</strong> ").append(booking.getId()).append("</p>");
        content.append("<p><strong>Số tiền thanh toán:</strong> ").append(booking.getTotalPrice()).append(" ₫</p>");

        // Hướng dẫn check-in
        content.append("<h2>Hướng dẫn check-in</h2>");
        content.append("<p>Vui lòng mang theo email này và CMND/CCCD để làm thủ tục nhận phòng.</p>");
        content.append("<p>Quý khách có thể check-in từ <strong>17:00</strong> ngày ").append(booking.getCheckInDate()).append(".</p>");
        content.append("<p>Chúng tôi luôn sẵn sàng hỗ trợ bạn qua số điện thoại hỗ trợ: <strong>0123 456 789</strong>.</p>");

        content.append("<p>Chúc bạn có một kỳ nghỉ tuyệt vời!</p>");
        content.append("<p>Trân trọng,</p>");
        content.append("<p>Đội ngũ Homestay Management</p>");

        return content.toString();
    }
}

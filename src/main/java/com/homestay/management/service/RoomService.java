package com.homestay.management.service;

import com.homestay.management.model.Room;
import com.homestay.management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Thư mục nơi lưu trữ hình ảnh
    private static String UPLOADED_FOLDER = "src/main/resources/static/uploads/rooms/";

    // Lấy danh sách tất cả các phòng
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Lấy phòng theo ID
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // Tạo phòng mới và lưu hình ảnh
    public Room createRoom(Room room, MultipartFile file) throws IOException {
        // Lưu file lên thư mục và lấy đường dẫn
        String imagePath = saveFile(file);

        // Cập nhật đường dẫn hình ảnh vào đối tượng phòng
        room.setImage(imagePath);

        // Lưu phòng vào cơ sở dữ liệu
        return roomRepository.save(room);
    }

    // Cập nhật phòng
    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id).orElseThrow();
        room.setName(roomDetails.getName());
        room.setType(roomDetails.getType());
        room.setPrice(roomDetails.getPrice());
        room.setAvailable(roomDetails.isAvailable());
        return roomRepository.save(room);
    }

    // Xóa phòng
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    // Phương thức để lưu hình ảnh vào thư mục
    private String saveFile(MultipartFile file) throws IOException {
        // Tạo thư mục nếu chưa có
        File dir = new File(UPLOADED_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Lưu file vào thư mục
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        return "/uploads/rooms/" + file.getOriginalFilename(); // Trả về đường dẫn hình ảnh trong thư mục static
    }
    
    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByType(type);
    }
    
    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailable(true);
    }

    public List<Room> getAvailableRoomsByType(String type) {
        return roomRepository.findByTypeAndAvailable(type, true);
    }
}

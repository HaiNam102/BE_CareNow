package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.BookedTimeSlotRes;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.DTO.Response.BookingRes;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Enum.EStatus;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.BookingService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;
    JwtUtil jwtUtil;
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingReq bookingReq,
                                           @RequestParam Long careTakerId,
                                           @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        Long customer_id = jwtUtil.extractUserId(token);
        Booking booking = bookingService.createBooking(bookingReq, careTakerId, customer_id);
        BookingDTO bookingDTO = bookingService.convertToBookingDTO(booking);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(bookingDTO)
                .build()
        );
    }
    
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(bookingService.getAllBookings())
                .build()
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(bookingService.getBookingById(id))
                .build()
        );
    }

    @GetMapping("/customer")
    public ResponseEntity<?> getCustomerBookings(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long customerId = jwtUtil.extractUserId(token);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(bookingService.getBookingsByCustomerId(customerId))
                .build()
        );
    }

    @GetMapping("/caretaker")
    public ResponseEntity<?> getCareTakerBookings(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long careTakerId = jwtUtil.extractUserId(token);

        List<BookingRes> bookings = bookingService.getBookingsByCareTakerId(careTakerId);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(bookings)
                .build()
        );
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<String> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        try {
            bookingService.updateBookingStatus(bookingId, EStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok("Booking " + status + " successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
    }

    /**
     * API lấy danh sách các time slot đã đặt của một careTaker cho một danh sách ngày
     * 
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param days danh sách ngày cần kiểm tra
     * @return danh sách BookedTimeSlotRes chứa thông tin về các time slot đã đặt
     */
    @GetMapping("/booked-slots/caretaker/{careTakerId}")
    public ResponseEntity<?> getBookedTimeSlots(
            @PathVariable Long careTakerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> days) {
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(bookingService.getBookedTimeSlotsForDays(careTakerId, days))
                .build()
        );
    }
}

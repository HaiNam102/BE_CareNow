package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.BookingService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

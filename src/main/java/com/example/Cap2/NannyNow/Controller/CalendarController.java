package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.CalendarRes;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CalendarService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CalendarController {
    CalendarService calendarService;
    JwtUtil jwtUtil;

    /**
     * API lấy tất cả lịch làm việc (calendar) của một careTaker theo ID
     * @param careTakerId ID của care taker
     * @return Danh sách lịch làm việc dưới dạng CalendarRes
     */
    @GetMapping("/caretaker/{careTakerId}")
    public ResponseEntity<?> getCalendarsByCareTakerId(@PathVariable Long careTakerId) {
        List<CalendarRes> calendars = calendarService.getCalendarsByCareTakerId(careTakerId);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(calendars)
                .build()
        );
    }
    
    /**
     * API lấy tất cả lịch làm việc (calendar) của careTaker hiện tại từ token JWT
     * @param authHeader Header Authorization chứa JWT token
     * @return Danh sách lịch làm việc dưới dạng CalendarRes
     */
    @GetMapping("/my-calendar")
    public ResponseEntity<?> getMyCalendars(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long careTakerId = jwtUtil.extractUserId(token);
        
        List<CalendarRes> calendars = calendarService.getCalendarsByCareTakerId(careTakerId);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(calendars)
                .build()
        );
    }
} 
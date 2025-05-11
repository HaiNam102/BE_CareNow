package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.CalendarReq;
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

    @PostMapping("/create")
    public ResponseEntity<?> getMyCalendars(@RequestHeader("Authorization") String authHeader,@RequestBody CalendarReq calendarReq) {
        String token = authHeader.replace("Bearer ", "");
        Long careTakerId = jwtUtil.extractUserId(token);

        List<CalendarRes> calendars = calendarService.createCalendar(careTakerId,calendarReq);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(calendars)
                .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable Long id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
} 
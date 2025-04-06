package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CareRecipientService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/careRecipient")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareRecipientController {
    CareRecipientService careRecipientService;
    JwtUtil jwtUtil;

    @GetMapping("/customer")
    public ResponseEntity<?> getCareRecipientsByCustomerId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long customerId = jwtUtil.extractUserId(token);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careRecipientService.getCareRecipientsByCustomerId(customerId))
                .build()
        );
    }
}

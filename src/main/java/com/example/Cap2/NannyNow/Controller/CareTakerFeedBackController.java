package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerFeedBackReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CareTakerFeedBackService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/careTakerFeedBack")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareTakerFeedBackController {
    CareTakerFeedBackService careTakerFeedBackService;
    JwtUtil jwtUtil;
    @PostMapping
    public ResponseEntity<?> SaveFeedBack(
            @RequestBody CareTakerFeedBackReq careTakerFeedBackReq,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("careTaker_id") Long careTaker_id
            ){
        String token = authHeader.replace("Bearer ", "");
        Long customer_id = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(careTakerFeedBackService.saveCareTakerFeedback(careTakerFeedBackReq,customer_id,careTaker_id))
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> getFeedBackByRatingAndCareTaker(
            @RequestParam("careTaker_id") Long careTaker_id
    ){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careTakerFeedBackService.getFeedBackOfCareTaker(careTaker_id))
                .build()
        );
    }
}

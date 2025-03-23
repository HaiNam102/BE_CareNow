package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CareTakerService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/careTaker")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareTakerController {
    CareTakerService careTakerService;

    @GetMapping
    public ResponseEntity<?> getAllCareTaker(){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careTakerService.getAllCareTaker())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCareTakerById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careTakerService.getCareTakerById(id))
                .build()
        );
    }

    @GetMapping("/search")
    public  ResponseEntity<?> getCareTakerByDayAndArea(@RequestParam("area") String area,
                                                       @RequestParam("dayStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dayStart,
                                                       @RequestParam(value = "dayEnd",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dayEnd){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careTakerService.getCareTakerByDayAndArea(area,dayStart,dayEnd))
                .build()
        );
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<?> getCareTakerRating(@PathVariable Long id) {
        float averageRating = careTakerService.calculateAverageRating(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(averageRating)
                .build()
        );
    }

    @GetMapping("/{id}/reviewers")
    public ResponseEntity<?> getCareTakerReviewers(@PathVariable Long id) {
        int totalReviewers = careTakerService.getTotalReviewers(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalReviewers", totalReviewers);
        result.put("rating", careTakerService.calculateAverageRating(id));
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(result)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCareTaker(@PathVariable Long id, @RequestBody CareTakerReq careTakerReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(careTakerService.updateCareTaker(id, careTakerReq))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCareTaker(@PathVariable Long id) {
        careTakerService.deleteCareTaker(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
}

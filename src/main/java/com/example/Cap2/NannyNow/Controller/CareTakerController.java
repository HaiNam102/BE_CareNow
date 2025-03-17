package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CareTakerService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
                                                       @RequestParam("dayEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dayEnd){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(careTakerService.getCareTakerByDayAndArea(area,dayStart,dayEnd))
                .build()
        );
    }
}

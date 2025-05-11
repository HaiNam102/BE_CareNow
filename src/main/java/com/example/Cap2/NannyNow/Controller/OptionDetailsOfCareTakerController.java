package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.OptionDetailsOfCareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.OptionDetailsOfCareTakerService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/option-details-of-caretaker")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionDetailsOfCareTakerController {
    OptionDetailsOfCareTakerService optionDetailsOfCareTakerService;

    @GetMapping
    public ResponseEntity<?> getAllOptionDetailsOfCareTaker() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionDetailsOfCareTakerService.getAllOptionDetailsOfCareTaker())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOptionDetailsOfCareTakerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionDetailsOfCareTakerService.getOptionDetailsOfCareTakerById(id))
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createOptionDetailsOfCareTaker(
            @RequestBody List<OptionDetailsOfCareTakerReq> requests) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(optionDetailsOfCareTakerService.createOptionDetailsOfCareTaker(requests))
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOptionDetailsOfCareTaker(
            @PathVariable Long id,
            @RequestBody OptionDetailsOfCareTakerReq req) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(optionDetailsOfCareTakerService.updateOptionDetailsOfCareTaker(id, req))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOptionDetailsOfCareTaker(@PathVariable Long id) {
        optionDetailsOfCareTakerService.deleteOptionDetailsOfCareTaker(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
}

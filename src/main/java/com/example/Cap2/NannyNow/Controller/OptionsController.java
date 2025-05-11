package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.OptionsReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.OptionsService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionsController {
    OptionsService optionsService;

    @GetMapping
    public ResponseEntity<?> getAllOptions() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionsService.getAllOptions())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOptionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionsService.getOptionById(id))
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createOption(@RequestBody OptionsReq optionsReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(optionsService.createOption(optionsReq))
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOption(@PathVariable Long id, @RequestBody OptionsReq optionsReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(optionsService.updateOption(id, optionsReq))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long id) {
        optionsService.deleteOption(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
}
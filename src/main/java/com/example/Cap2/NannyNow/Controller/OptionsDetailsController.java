package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.OptionsDetailsReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.OptionsDetailsService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options-details")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionsDetailsController {
    OptionsDetailsService optionsDetailsService;

    @GetMapping
    public ResponseEntity<?> getAllOptionsDetails() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionsDetailsService.getAllOptionsDetails())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOptionsDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(optionsDetailsService.getOptionsDetailsById(id))
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createOptionsDetail(@RequestBody OptionsDetailsReq optionsDetailsReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(optionsDetailsService.createOptionsDetails(optionsDetailsReq))
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOptionsDetail(@PathVariable Long id, @RequestBody OptionsDetailsReq optionsDetailsReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(optionsDetailsService.updateOptionsDetails(id, optionsDetailsReq))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOptionsDetail(@PathVariable Long id) {
        optionsDetailsService.deleteOptionsDetails(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
}


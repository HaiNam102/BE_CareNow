package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.CustomerReq;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.CustomerService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {
    CustomerService customerService;
    JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(customerService.getAllCustomers())
                .build()
        );
    }

    @GetMapping("/getByCustomerId")
    public ResponseEntity<?> getCustomerById(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long customerId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(customerService.getCustomerById(customerId))
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerReq customerReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(customerService.createCustomer(customerReq))
                .build()
        );
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestHeader("Authorization") String authHeader,@RequestBody CustomerReq customerReq) {
        String token = authHeader.replace("Bearer ", "");
        Long customerId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(customerService.updateCustomer(customerId, customerReq))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .build()
        );
    }
}

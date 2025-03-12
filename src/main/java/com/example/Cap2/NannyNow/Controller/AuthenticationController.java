package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.Author.LoginDTO;
import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.AuthenticationResponse;
import com.example.Cap2.NannyNow.Entity.Account;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.AccountService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auths")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AccountService accountService;
    JwtUtil jwtUtil;

    public Account authenticate(String userName, String password) {
        try {
            Account account = accountService.getAccountByUserName(userName);
            if (account == null) {
                throw new ApiException(ErrorCode.INVALID_USERNAME);
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(password, account.getPassword())) {
                throw new ApiException(ErrorCode.INVALID_PASSWORD);
            }
            return account;
        } catch (Exception e) {
            throw new RuntimeException("Invalid credential", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        Account account = authenticate(request.getUsername(), request.getPassword());
        if (account == null) {
            throw new ApiException(ErrorCode.INVALID_ACCOUNT);
        }
        String roleName = account.getAccountRoles().get(0).getRole().getRoleName();
        if (roleName == null || roleName.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }
        String token = jwtUtil.generateToken(account.getUserName(), roleName);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> register(
            @RequestPart("registerDTO") RegisterDTO registerDTO,
            @RequestParam(value = "imgProfile", required = false) MultipartFile imgProfile,
            @RequestParam(value = "imgCccd", required = false) MultipartFile imgCccd
    ) throws IOException {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(SuccessCode.REGISTER_SUCCESS.getCode())
                        .message(SuccessCode.REGISTER_SUCCESS.getMessage())
                        .data(accountService.register(registerDTO, imgProfile, imgCccd))
                        .build()
        );
    }
}

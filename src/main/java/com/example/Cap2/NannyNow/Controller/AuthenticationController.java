package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.Author.LoginDTO;
import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.AuthenticationResponse;
import com.example.Cap2.NannyNow.Entity.Account;
import com.example.Cap2.NannyNow.Entity.Account_Role;
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
import java.util.List;

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
        List<Account_Role> roles = account.getAccountRoles();
        if (roles == null || roles.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        String roleName = roles.get(0).getRole().getRoleName();
        Long userId = null;

        switch (roleName.toUpperCase()) {
            case "CUSTOMER":
                if (account.getCustomer() != null) {
                    userId = account.getCustomer().getCustomer_id();
                }
                break;
            case "CARE_TAKER":
                if (account.getCareTaker() != null) {
                    userId = account.getCareTaker().getCare_taker_id();
                }
                break;
            case "ADMIN":
                if (account.getAdmin() != null) {
                    userId = account.getAdmin().getAdminId();
                }
                break;
            default:
                throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        if (userId == null) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }

        String token = jwtUtil.generateToken(account.getAccountId(), userId, roleName);
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
            .jwt(token)
            .userId(userId)
            .roleName(roleName)
            .build();
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.LOGIN_SUCCESS.getCode())
                .message(SuccessCode.LOGIN_SUCCESS.getMessage())
                .data(authResponse)
                .build());
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

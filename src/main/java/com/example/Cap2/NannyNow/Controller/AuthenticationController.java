package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.LoginDTO;
import com.example.Cap2.NannyNow.DTO.Response.AuthenticationResponse;
import com.example.Cap2.NannyNow.Entity.Account;
import com.example.Cap2.NannyNow.Service.AccountService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auths")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AccountService accountService;
    JwtUtil jwtUtil;

    public Account authenticate(String userName, String password) {
        try {
            System.out.println("Attempting to authenticate user: " + userName);
            Account account = accountService.getAccountByUserName(userName);

            if (account == null) {
                throw new RuntimeException("Invalid username");
            }

            System.out.println("Account found. Checking password...");

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(password, account.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            return account;

        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage());
            throw new RuntimeException("Invalid credential", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        System.out.println("Starting authentication...");

        Account account = authenticate(request.getUsername(), request.getPassword());
        if (account == null) {
            System.out.println("Account not found or invalid credentials");
            throw new RuntimeException("Invalid credential account");
        }

        String roleName = account.getAccountRoles().get(0).getRole().getRoleName();
        if (roleName == null || roleName.isEmpty()) {
            System.out.println("Role not found or invalid role name");
            throw new RuntimeException("Invalid credential rolename");
        }

        String token = jwtUtil.generateToken(account.getUserName(), roleName);

        System.out.println("Token generated successfully");

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}

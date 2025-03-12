package com.example.Cap2.NannyNow.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    INVALID_USERNAME(40400, "invalid username", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(40401, "invalid password", HttpStatus.NOT_FOUND),
    INVALID_ACCOUNT(40402, "invalid account", HttpStatus.NOT_FOUND),
    INVALID_ROLE(40403, "invalid role", HttpStatus.NOT_FOUND),
    MAIL_PHONE_USERNAME_ALREADY_EXITS(40900, "userName, Mail or Phone already exits ", HttpStatus.CONFLICT),
    IMAGE_UPLOAD_FAILED(40409,"Image upload fail",HttpStatus.CONFLICT),
    USER_NOT_FOUND(40400, "customer or care taker not found", HttpStatus.NOT_FOUND),
    ;
    Integer code;
    String message;
    HttpStatus status;
}

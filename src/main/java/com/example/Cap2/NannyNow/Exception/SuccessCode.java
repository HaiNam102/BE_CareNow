package com.example.Cap2.NannyNow.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum SuccessCode {
    REGISTER_SUCCESS(20000,"Register success",HttpStatus.CREATED),
    LOGIN_SUCCESS(20001,"Login success",HttpStatus.CREATED),
    GET_SUCCESSFUL(1010, "Get successful", HttpStatus.OK),
    ADD_SUCCESSFUL(1011, "Add successful data", HttpStatus.OK),
    UPDATE_SUCCESSFUL(1012, "Update successful", HttpStatus.OK),
    DELETE_SUCCESSFUL(1013, "Delete successful", HttpStatus.OK);

    Integer code;
    String message;
    HttpStatus status;
}

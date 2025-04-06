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
    USER_NOT_FOUND(40404, "customer or care taker not found", HttpStatus.NOT_FOUND),
    INVALID_BOOKING(40405, "invalid username", HttpStatus.NOT_FOUND),
    CARE_RECIPIENT_NOT_FOUND(40406, "care recipient not found", HttpStatus.NOT_FOUND),
    CARE_RECIPIENT_NOT_BELONG_TO_CUSTOMER(40407, "care recipient does not belong to this customer", HttpStatus.FORBIDDEN),
    MAIL_PHONE_USERNAME_ALREADY_EXITS(40900, "userName, Mail or Phone already exits ", HttpStatus.CONFLICT),
    IMAGE_UPLOAD_FAILED(40901,"Image upload fail",HttpStatus.CONFLICT),
    BOOKING_REQUEST(40902, "Booking cannot be made during this time." +
            "Please select a time that is at least 1 hour after the care taker has been off.", HttpStatus.CONFLICT),
    BOOKING_TIME_CONFLICT(40903, "Thời gian này đã có người đặt. Vui lòng chọn thời gian khác.", HttpStatus.CONFLICT),
    OPTION_DETAIL_NOT_FOUND(404055,"Option detail not found",HttpStatus.NOT_FOUND)
    ;
    Integer code;
    String message;
    HttpStatus status;
}

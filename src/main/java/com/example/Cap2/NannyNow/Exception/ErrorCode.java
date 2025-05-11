package com.example.Cap2.NannyNow.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
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
    OPTION_NOT_FOUND(404056, "Option not found", HttpStatus.NOT_FOUND),
    OPTION_DETAIL_OF_CARETAKER_NOT_FOUND(404055, "Option detail of caretaker not found", HttpStatus.NOT_FOUND),
    CHAT_ROOM_NOT_FOUND(40404, "Chat room not found", HttpStatus.NOT_FOUND),
    INVALID_USER_TYPE(40405, "Invalid user type. Must be either CUSTOMER or CARE_TAKER", HttpStatus.BAD_REQUEST)
    BOOKING_TIME_CONFLICT(40903, "Thời gian này đã có người đặt. Vui lòng chọn thời gian khác.", HttpStatus.CONFLICT),
    BOOKING_TIME_TOO_CLOSE(40904, "Thời gian đặt lịch phải cách các lịch khác ít nhất 1 tiếng", HttpStatus.CONFLICT),
    BOOKING_NOT_FOUND(40905, "Không tìm thấy booking", HttpStatus.NOT_FOUND),
    OPTION_DETAIL_NOT_FOUND(404055, "Option detail not found", HttpStatus.NOT_FOUND),
    CALENDAR_NOT_FOUND(404056, "Calendar not found", HttpStatus.NOT_FOUND),
    INVALID_CCCD(40408,"invalid CCCD",HttpStatus.NOT_FOUND),
    ACCOUNT_INACTIVE(40409,"Your account has been blocked",HttpStatus.CONFLICT),
    ACCOUNT_NOT_FOUND(40409,"Account not found",HttpStatus.NOT_FOUND),
    INVALID_DOB(40408,"DOB not match ",HttpStatus.NOT_FOUND),
    INVALID_GENDER(40408,"Gender not match ",HttpStatus.NOT_FOUND)
    ;
    
    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}

package com.example.Cap2.NannyNow.DTO.Request;

import com.example.Cap2.NannyNow.Enum.ELocationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingReq {
    String placeName;
    ELocationType locationType;
    String bookingAddress;
    String descriptionPlace;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    List<LocalDate> days;
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToStart;
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToEnd;
    String jobDescription;
    Long careRecipientId;
    float price;
}

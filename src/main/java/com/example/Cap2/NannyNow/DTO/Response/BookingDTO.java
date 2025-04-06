package com.example.Cap2.NannyNow.DTO.Response;

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
public class BookingDTO {
    Long bookingId;
    ELocationType locationType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate createdAt;
    String serviceProgress;
    String careTakerName;
    float rating;
    int toltalReviewers;
    Long careRecipientId;
    String careRecipientName;
} 
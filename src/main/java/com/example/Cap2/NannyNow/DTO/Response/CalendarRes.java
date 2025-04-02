package com.example.Cap2.NannyNow.DTO.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalendarRes {
    Long calendarId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate day;
    
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToStart;
    
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToEnd;

} 
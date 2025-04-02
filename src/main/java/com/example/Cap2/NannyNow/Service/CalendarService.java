package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Response.CalendarRes;
import com.example.Cap2.NannyNow.Entity.Calendar;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CalendarMapper;
import com.example.Cap2.NannyNow.Repository.CalendarRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CalendarService {
    CalendarRepository calendarRepository;
    CareTakerRepository careTakerRepository;
    CalendarMapper calendarMapper;

    public List<CalendarRes> getCalendarsByCareTakerId(Long careTakerId) {
        CareTaker careTaker = careTakerRepository.findById(careTakerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        List<Calendar> calendars = calendarRepository.findByCareTakerId(careTakerId);
        return calendars.stream()
                .map(calendarMapper::toCalendarRes)
                .collect(Collectors.toList());
    }
} 
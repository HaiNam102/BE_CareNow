package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CalendarReq;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CalendarService {
    CalendarRepository calendarRepository;
    CareTakerRepository careTakerRepository;
    CalendarMapper calendarMapper;

    /**
     * Lấy tất cả lịch làm việc (calendar) của một careTaker
     * @param careTakerId ID của care taker
     * @return Danh sách CalendarRes chứa thông tin lịch làm việc
     */
    public List<CalendarRes> getCalendarsByCareTakerId(Long careTakerId) {
        CareTaker careTaker = careTakerRepository.findById(careTakerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
                
        // Lấy danh sách các calendar của careTaker
        List<Calendar> calendars = calendarRepository.findByCareTakerId(careTakerId);
        
        // Chuyển đổi sang DTO và trả về
        return calendars.stream()
                .map(calendarMapper::toCalendarRes)
                .collect(Collectors.toList());
    }

    public List<CalendarRes> createCalendar(Long careTakerId,CalendarReq calendarReq){
        CareTaker careTaker = careTakerRepository.findById(careTakerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        List<CalendarRes> calendarRes = new ArrayList<>();
        for(LocalDate day : calendarReq.getDay()){
            Calendar calendars = new Calendar();
            calendars.setDay(day);
            calendars.setCare_taker(careTaker);
            calendarRepository.save(calendars);
            CalendarRes calendarRes1 = new CalendarRes();
            calendarRes1.setDay(day);
            calendarRes.add(calendarRes1);
        }
        return calendarRes;
    }
} 
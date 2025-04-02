package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Response.CalendarRes;
import com.example.Cap2.NannyNow.Entity.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CalendarMapper {
    CalendarMapper INSTANCE = Mappers.getMapper(CalendarMapper.class);

    CalendarRes toCalendarRes(Calendar calendar);
} 
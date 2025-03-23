package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.Entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "bookingDays", ignore = true)
    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "care_taker", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Booking toBooking(BookingReq bookingReq);
}

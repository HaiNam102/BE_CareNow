package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookingRes;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.BookingDay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "bookingDays", ignore = true)
    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "care_taker", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Booking toBooking(BookingReq bookingReq);

    @Mapping(source = "care_taker.nameOfCareTaker", target = "careTakerName")
    @Mapping(source = "customer.nameOfCustomer", target = "customerName")
    @Mapping(source = "bookingDays", target = "days", qualifiedByName = "mapBookingDaysToLocalDate")
    @Mapping(source = "payment.price", target = "servicePrice")
    BookingRes toBookingRes(Booking booking);

    @Named("mapBookingDaysToLocalDate")
    default List<LocalDate> mapBookingDaysToLocalDate(List<BookingDay> bookingDays) {
        if (bookingDays == null) {
            return null;
        }
        return bookingDays.stream()
                .map(BookingDay::getDay)
                .collect(Collectors.toList());
    }
}

package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
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
    @Mapping(target = "careRecipient", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(source = "price", target = "payment.price")
    Booking toBooking(BookingReq bookingReq);

    @Mapping(source = "care_taker.nameOfCareTaker", target = "careTakerName")
    @Mapping(source = "care_taker.care_taker_id", target = "careTakerId")
    @Mapping(source = "care_taker.image.imgProfile" ,target = "imgProfile")
    @Mapping(source = "customer.nameOfCustomer", target = "customerName")
    @Mapping(source = "bookingDays", target = "days", qualifiedByName = "mapBookingDaysToLocalDate")
    @Mapping(source = "payment.price", target = "servicePrice")
    @Mapping(source = "serviceProgress", target = "serviceProgress")
    @Mapping(source = "careRecipient.careRecipientId", target = "careRecipientId")
    @Mapping(source = "careRecipient.name", target = "careRecipientName")
    @Mapping(source = "payment.status",target = "status")

    BookingRes toBookingRes(Booking booking);

    @Mapping(source = "care_taker.image.imgProfile" ,target = "imgProfile")
    BookingDTO toBookingDTO(Booking boooking);

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

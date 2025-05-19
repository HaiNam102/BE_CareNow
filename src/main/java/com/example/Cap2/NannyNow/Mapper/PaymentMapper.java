package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Response.PaymentRes;
import com.example.Cap2.NannyNow.DTO.Response.PaymentResAdmin;
import com.example.Cap2.NannyNow.Entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "booking.bookingId",target = "bookingId")
    @Mapping(source = "booking.serviceProgress",target = "bookingStatus")
    PaymentRes toPaymentRes(Payment payment);

    @Mapping(source = "booking.customer.nameOfCustomer",target = "nameOfUser")
    PaymentResAdmin toPaymentResAdmin(Payment payment);
}

package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.BookingMapper;
import com.example.Cap2.NannyNow.Repository.BookingRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    CareTakerRepository careTakerRepository;
    CustomerRepository customerRepository;
    @Autowired
    private ChatService chatService;

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime){
        List<Booking> bookings = bookingRepository.findBookingForDay(careTakerId,day);
        LocalTime lastedEndTime = null;

        for(Booking booking : bookings){
            if(lastedEndTime == null || booking.getTimeToEnd().isAfter(lastedEndTime)){
                lastedEndTime = booking.getTimeToEnd();
            }
        }

        if(lastedEndTime == null){
            return true;
        }

        return requestedStartTime.isAfter(lastedEndTime.plusHours(1));
    }

    public Booking createBooking(BookingReq bookingReq, Long careTakerId, Long customerId){
        CareTaker careTaker = careTakerRepository.findById(careTakerId).orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));

        if(!isValidBooking(careTakerId,bookingReq.getDay(),bookingReq.getTimeToStart())){
            throw new ApiException(ErrorCode.BOOKING_REQUEST);
        }
        Booking booking = bookingMapper.toBooking(bookingReq);
        booking.setCustomer(customer);
        booking.setCare_taker(careTaker);
        bookingRepository.save(booking);

        // Automatically create a chat room for this booking
        chatService.createChatRoom(customerId, careTakerId);

        return booking;
    }
}

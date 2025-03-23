package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.BookingDay;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.BookingMapper;
import com.example.Cap2.NannyNow.Repository.BookingDayRepository;
import com.example.Cap2.NannyNow.Repository.BookingRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {
    BookingRepository bookingRepository;
    BookingDayRepository bookingDayRepository;
    BookingMapper bookingMapper;
    CareTakerRepository careTakerRepository;
    CustomerRepository customerRepository;

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime) {
        List<Booking> bookings = bookingRepository.findBookingForDay(careTakerId, day);
        LocalTime lastedEndTime = null;

        for (Booking booking : bookings) {
            if (lastedEndTime == null || booking.getTimeToEnd().isAfter(lastedEndTime)) {
                lastedEndTime = booking.getTimeToEnd();
            }
        }

        if (lastedEndTime == null) {
            return true;
        }

        return requestedStartTime.isAfter(lastedEndTime.plusHours(1));
    }
    
    public boolean areAllDaysValid(Long careTakerId, List<LocalDate> days, LocalTime requestedStartTime) {
        for (LocalDate day : days) {
            if (!isValidBooking(careTakerId, day, requestedStartTime)) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Booking createBooking(BookingReq bookingReq, Long careTakerId, Long customerId) {
        CareTaker careTaker = careTakerRepository.findById(careTakerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!areAllDaysValid(careTakerId, bookingReq.getDays(), bookingReq.getTimeToStart())) {
            throw new ApiException(ErrorCode.BOOKING_REQUEST);
        }

        Booking booking = bookingMapper.toBooking(bookingReq);
        booking.setCustomer(customer);
        booking.setCare_taker(careTaker);
        booking.setBookingDays(new ArrayList<>());

        booking = bookingRepository.save(booking);

        for (LocalDate day : bookingReq.getDays()) {
            BookingDay bookingDay = new BookingDay();
            bookingDay.setDay(day);
            bookingDay.setBooking(booking);
            booking.getBookingDays().add(bookingDay);
        }

        return bookingRepository.save(booking);
    }
    
    public BookingDTO convertToBookingDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setPlaceName(booking.getPlaceName());
        bookingDTO.setLocationType(booking.getLocationType());
        bookingDTO.setBookingAddress(booking.getBookingAddress());
        bookingDTO.setDescriptionPlace(booking.getDescriptionPlace());
        bookingDTO.setTimeToStart(booking.getTimeToStart());
        bookingDTO.setTimeToEnd(booking.getTimeToEnd());
        bookingDTO.setServiceProgress(booking.getServiceProgress());

        if (booking.getCustomer() != null) {
            bookingDTO.setCustomerId(booking.getCustomer().getCustomer_id());
            bookingDTO.setCustomerName(booking.getCustomer().getNameOfCustomer());
        }

        if (booking.getCare_taker() != null) {
            bookingDTO.setCareTakerId(booking.getCare_taker().getCare_taker_id());
            bookingDTO.setCareTakerName(booking.getCare_taker().getNameOfCareTaker());
        }

        if (booking.getBookingDays() != null && !booking.getBookingDays().isEmpty()) {
            List<LocalDate> days = booking.getBookingDays().stream()
                    .map(BookingDay::getDay)
                    .collect(Collectors.toList());
            bookingDTO.setDays(days);
        } else {
            bookingDTO.setDays(new ArrayList<>());
        }
        
        return bookingDTO;
    }
    
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }
    
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return convertToBookingDTO(booking);
    }
}

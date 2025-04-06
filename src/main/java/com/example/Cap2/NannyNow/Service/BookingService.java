package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.DTO.Response.BookingRes;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.BookingDay;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Entity.CareRecipient;
import com.example.Cap2.NannyNow.Enum.EStatus;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.BookingMapper;
import com.example.Cap2.NannyNow.Repository.BookingDayRepository;
import com.example.Cap2.NannyNow.Repository.BookingRepository;
import com.example.Cap2.NannyNow.Repository.CareRecipientRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    CareTakerService careTakerService;
    CareRecipientRepository careRecipientRepository;

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

        // Kiểm tra và lấy thông tin người được chăm sóc (CareRecipient)
        CareRecipient careRecipient = null;
        if (bookingReq.getCareRecipientId() != null) {
            careRecipient = careRecipientRepository.findById(bookingReq.getCareRecipientId())
                    .orElseThrow(() -> new ApiException(ErrorCode.CARE_RECIPIENT_NOT_FOUND));
            
            // Kiểm tra xem người được chăm sóc có thuộc về khách hàng này không
            if (!careRecipient.getCustomer().getCustomer_id().equals(customerId)) {
                throw new ApiException(ErrorCode.CARE_RECIPIENT_NOT_BELONG_TO_CUSTOMER);
            }
        }

        if (!areAllDaysValid(careTakerId, bookingReq.getDays(), bookingReq.getTimeToStart())) {
            throw new ApiException(ErrorCode.BOOKING_REQUEST);
        }

        Booking booking = bookingMapper.toBooking(bookingReq);
        booking.setCustomer(customer);
        booking.setCare_taker(careTaker);
        booking.setCareRecipient(careRecipient);
        booking.setBookingDays(new ArrayList<>());
        booking.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        booking.setServiceProgress(EStatus.PENDING);
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
        bookingDTO.setLocationType(booking.getLocationType());
        bookingDTO.setServiceProgress(String.valueOf(booking.getServiceProgress()));
        careTakerService.updateAverageRating(booking.getCare_taker());
        bookingDTO.setRating(booking.getCare_taker().getAvarageRating());
        bookingDTO.setToltalReviewers(careTakerService.getTotalReviewers(booking.getCare_taker().getCare_taker_id()));
        
        if (booking.getCare_taker() != null) {
            bookingDTO.setCareTakerName(booking.getCare_taker().getNameOfCareTaker());
        }
        
        // Thêm thông tin về care recipient
        if (booking.getCareRecipient() != null) {
            bookingDTO.setCareRecipientId(booking.getCareRecipient().getCareRecipientId());
            bookingDTO.setCareRecipientName(booking.getCareRecipient().getName());
        }
        
        bookingDTO.setCreatedAt(booking.getCreatedAt());
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

    public List<BookingDTO> getBookingsByCustomerId(Long customerId) {
        List<Booking> bookings = bookingRepository.findBookingsByCustomerId(customerId);
        return bookings.stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public List<BookingRes> getBookingsByCareTakerId(Long careTakerId) {
        List<Booking> bookings = bookingRepository.findByCareTakerId(careTakerId);
        return bookings.stream()
                .map(bookingMapper::toBookingRes)
                .collect(Collectors.toList());
    }

    public Booking updateBookingStatus(Long bookingId, EStatus status) {
        Booking optionalBooking = bookingRepository.findById(bookingId).orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        if (optionalBooking != null) {
            optionalBooking.setServiceProgress(status);
            return bookingRepository.save(optionalBooking);
        }
        throw new RuntimeException("Booking not found");
    }


}

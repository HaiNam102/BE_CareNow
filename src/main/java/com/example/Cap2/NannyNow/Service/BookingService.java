package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookedTimeSlotRes;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.DTO.Response.BookingRes;
import com.example.Cap2.NannyNow.Entity.*;
import com.example.Cap2.NannyNow.Enum.EStatus;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.BookingMapper;
import com.example.Cap2.NannyNow.Repository.*;
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
    PaymentRepository paymentRepository;

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime, LocalTime requestedEndTime) {
        List<Booking> bookings = bookingRepository.findBookingForDay(careTakerId, day);
        
        // Kiểm tra xem thời gian định đặt có trùng với booking nào đã tồn tại không
        for (Booking booking : bookings) {
            LocalTime existingStartTime = booking.getTimeToStart();
            LocalTime existingEndTime = booking.getTimeToEnd();
            
            // Trường hợp 1: Thời gian bắt đầu của booking mới nằm trong khoảng thời gian của booking đã tồn tại
            // requestedStartTime >= existingStartTime và requestedStartTime < existingEndTime
            boolean startTimeOverlap = !requestedStartTime.isBefore(existingStartTime) && requestedStartTime.isBefore(existingEndTime);
            
            // Trường hợp 2: Thời gian kết thúc của booking mới nằm trong khoảng thời gian của booking đã tồn tại
            // requestedEndTime > existingStartTime và requestedEndTime <= existingEndTime
            boolean endTimeOverlap = requestedEndTime.isAfter(existingStartTime) && !requestedEndTime.isAfter(existingEndTime);
            
            // Trường hợp 3: Booking mới bao trùm booking đã tồn tại
            // requestedStartTime <= existingStartTime và requestedEndTime >= existingEndTime
            boolean bookingCoversExisting = !requestedStartTime.isAfter(existingStartTime) && !requestedEndTime.isBefore(existingEndTime);
            
            if (startTimeOverlap || endTimeOverlap || bookingCoversExisting) {
                return false;
            }
        }
        
        return true;
    }

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime) {
        // Mặc định thời gian kết thúc là 1 giờ sau thời gian bắt đầu
        LocalTime requestedEndTime = requestedStartTime.plusHours(1);
        return isValidBooking(careTakerId, day, requestedStartTime, requestedEndTime);
    }

    private boolean areAllDaysValid(Long careTakerId, List<LocalDate> days, LocalTime startTime, LocalTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingStart = LocalDateTime.of(days.get(0), startTime);
        
        // Kiểm tra thời gian đặt phải cách thời gian hiện tại ít nhất 1 tiếng
        if (bookingStart.isBefore(now.plusHours(1))) {
            throw new ApiException(ErrorCode.BOOKING_TIME_TOO_CLOSE);
        }
        
        // Kiểm tra xem có booking nào trùng thời gian không
        for (LocalDate day : days) {
            if (bookingRepository.existsOverlappingBooking(careTakerId, day, startTime, endTime)) {
                throw new ApiException(ErrorCode.BOOKING_TIME_CONFLICT);
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

        CareRecipient careRecipient = null;
        if (bookingReq.getCareRecipientId() != null) {
            careRecipient = careRecipientRepository.findById(bookingReq.getCareRecipientId())
                    .orElseThrow(() -> new ApiException(ErrorCode.CARE_RECIPIENT_NOT_FOUND));

            if (!careRecipient.getCustomer().getCustomer_id().equals(customerId)) {
                throw new ApiException(ErrorCode.CARE_RECIPIENT_NOT_BELONG_TO_CUSTOMER);
            }
        }

        if (!areAllDaysValid(careTakerId, bookingReq.getDays(), bookingReq.getTimeToStart(), bookingReq.getTimeToEnd())) {
            throw new ApiException(ErrorCode.BOOKING_TIME_CONFLICT);
        }

        Booking booking = bookingMapper.toBooking(bookingReq);
        booking.setCustomer(customer);
        booking.setCare_taker(careTaker);
        booking.setCareRecipient(careRecipient);
        booking.setBookingDays(new ArrayList<>());
        booking.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        booking.setServiceProgress(EStatus.PENDING);

        Payment payment = new Payment();
        payment.setStatus(false);
        payment.setPrice(bookingReq.getPrice());
        payment.setBooking(booking);
        payment.setCreateAt(LocalDateTime.now());
        booking.setPayment(payment);

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

    /**
     * Lấy danh sách các time slot đã đặt của một careTaker trong một ngày cụ thể
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param day ngày cần kiểm tra
     * @return danh sách BookedTimeSlotRes chứa thông tin về các time slot đã đặt
     */
    public List<BookedTimeSlotRes> getBookedTimeSlots(Long careTakerId, LocalDate day) {
        List<Booking> bookings = bookingRepository.findBookingForDay(careTakerId, day);
        return bookings.stream()
                .map(booking -> {
                    BookedTimeSlotRes timeSlot = new BookedTimeSlotRes();
                    timeSlot.setDay(day);
                    timeSlot.setTimeToStart(booking.getTimeToStart());
                    timeSlot.setTimeToEnd(booking.getTimeToEnd());
                    timeSlot.setStatus(booking.getServiceProgress().toString());
                    return timeSlot;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy danh sách các time slot đã đặt của một careTaker trong một khoảng ngày
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param startDay ngày bắt đầu
     * @param endDay ngày kết thúc
     * @return danh sách BookedTimeSlotRes chứa thông tin về các time slot đã đặt
     */
    public List<BookedTimeSlotRes> getBookedTimeSlotsInRange(Long careTakerId, LocalDate startDay, LocalDate endDay) {
        List<BookedTimeSlotRes> result = new ArrayList<>();
        LocalDate currentDay = startDay;
        
        while (!currentDay.isAfter(endDay)) {
            result.addAll(getBookedTimeSlots(careTakerId, currentDay));
            currentDay = currentDay.plusDays(1);
        }
        
        return result;
    }
}

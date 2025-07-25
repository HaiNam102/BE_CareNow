package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.BookingReq;
import com.example.Cap2.NannyNow.DTO.Response.BookedTimeSlotRes;
import com.example.Cap2.NannyNow.DTO.Response.BookingDTO;
import com.example.Cap2.NannyNow.DTO.Response.BookingRes;
import com.example.Cap2.NannyNow.DTO.Response.MonthlyStatsDTO;
import com.example.Cap2.NannyNow.Entity.*;
import com.example.Cap2.NannyNow.Enum.EStatus;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.BookingMapper;
import com.example.Cap2.NannyNow.Repository.*;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    CareTakerService careTakerService;
    CareRecipientRepository careRecipientRepository;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime,
            LocalTime requestedEndTime) {
        List<Booking> bookings = bookingRepository.findBookingForDay(careTakerId, day);

        // Kiểm tra xem thời gian định đặt có trùng hoặc quá gần với booking nào đã tồn
        // tại không
        for (Booking booking : bookings) {
            LocalTime existingStartTime = booking.getTimeToStart();
            LocalTime existingEndTime = booking.getTimeToEnd();

            // Kiểm tra trường hợp trùng thời gian
            boolean isTimeOverlap = requestedStartTime.isBefore(existingEndTime)
                    && requestedEndTime.isAfter(existingStartTime);

            if (isTimeOverlap) {
                throw new ApiException(ErrorCode.BOOKING_TIME_CONFLICT);
            }

            // Kiểm tra trường hợp không cách 1 tiếng
            boolean isTooClose = requestedStartTime.isBefore(existingEndTime.plusHours(1)) &&
                    requestedEndTime.isAfter(existingStartTime.minusHours(1));

            if (isTooClose) {
                throw new ApiException(ErrorCode.BOOKING_TIME_TOO_CLOSE);
            }
        }

        return true;
    }

    public boolean isValidBooking(Long careTakerId, LocalDate day, LocalTime requestedStartTime) {
        // Mặc định thời gian kết thúc là 1 giờ sau thời gian bắt đầu
        LocalTime requestedEndTime = requestedStartTime.plusHours(1);
        return isValidBooking(careTakerId, day, requestedStartTime, requestedEndTime);
    }

    public boolean areAllDaysValid(Long careTakerId, List<LocalDate> days, LocalTime requestedStartTime,
            LocalTime requestedEndTime) {
        for (LocalDate day : days) {
            if (!isValidBooking(careTakerId, day, requestedStartTime, requestedEndTime)) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllDaysValid(Long careTakerId, List<LocalDate> days, LocalTime requestedStartTime) {
        LocalTime requestedEndTime = requestedStartTime.plusHours(1);
        return areAllDaysValid(careTakerId, days, requestedStartTime, requestedEndTime);
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

        if (!areAllDaysValid(careTakerId, bookingReq.getDays(), bookingReq.getTimeToStart(),
                bookingReq.getTimeToEnd())) {
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

        Booking savedBooking = bookingRepository.save(booking);

        scheduler.schedule(() -> {
            Booking checkBooking = bookingRepository.findById(savedBooking.getBookingId())
                    .orElse(null);
            if (checkBooking != null && checkBooking.getServiceProgress() == EStatus.PENDING) {
                checkBooking.setServiceProgress(EStatus.REJECT);
                bookingRepository.save(checkBooking);
            }
        }, 10, TimeUnit.MINUTES);

        return savedBooking;

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
        bookingDTO.setServicePrice(booking.getPayment().getPrice());
        bookingDTO.setImgProfile(booking.getCare_taker().getImage().getImgProfile());
        bookingDTO.setStatus(booking.getPayment().getStatus());

        if (booking.getCare_taker() != null) {
            bookingDTO.setCareTakerName(booking.getCare_taker().getNameOfCareTaker());
        }

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

    public BookingRes getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return bookingMapper.toBookingRes(booking);
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
        Booking optionalBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if (optionalBooking != null) {
            optionalBooking.setServiceProgress(status);
            return bookingRepository.save(optionalBooking);
        }
        throw new RuntimeException("Booking not found");
    }

    /**
     * Lấy danh sách các time slot đã đặt của một careTaker trong một ngày cụ thể
     * 
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param day         ngày cần kiểm tra
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
     * 
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param startDay    ngày bắt đầu
     * @param endDay      ngày kết thúc
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

    /**
     * Lấy danh sách các time slot đã đặt của một careTaker trong một danh sách ngày
     * 
     * @param careTakerId ID của careTaker cần kiểm tra
     * @param days        danh sách ngày cần kiểm tra
     * @return danh sách BookedTimeSlotRes chứa thông tin về các time slot đã đặt
     */
    public List<BookedTimeSlotRes> getBookedTimeSlotsForDays(Long careTakerId, List<LocalDate> days) {
        List<BookedTimeSlotRes> result = new ArrayList<>();

        for (LocalDate day : days) {
            result.addAll(getBookedTimeSlots(careTakerId, day));
        }

        return result;
    }

    public CareRecipient getCareRecipientByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getCareRecipient() == null) {
            throw new ApiException(ErrorCode.CARE_RECIPIENT_NOT_FOUND);
        }
        return booking.getCareRecipient();
    }

    public List<MonthlyStatsDTO> getAllMonthlyStats() {
        List<Object[]> results = bookingRepository.getMonthlyStats();
        List<MonthlyStatsDTO> stats = new ArrayList<>();

        for (Object[] row : results) {
            int month = (int) row[0];
            int year = (int) row[1];
            long count = (Long) row[2];
            double revenue = row[3] != null ? (Double) row[3] : 0.0;

            stats.add(new MonthlyStatsDTO(month, year, count, revenue));
        }
        return stats;
    }

    public Double getCurrentMonthRevenueByCareTakerId(Long careTakerId) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        Double revenue = bookingRepository.getMonthlyRevenueByNannyId(careTakerId, month, year);
        return revenue != null ? revenue * 0.7 : 0.0;
    }

    public Booking getBookingEntityById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND));
    }
}

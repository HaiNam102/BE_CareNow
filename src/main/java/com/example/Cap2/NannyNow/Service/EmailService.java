package com.example.Cap2.NannyNow.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.Cap2.NannyNow.Repository.AdminRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Entity.Booking;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final CustomerRepository customerRepository;
    private final CareTakerRepository careTakerRepository;
    private final AdminRepository adminRepository;
    private final BookingService bookingService;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public EmailService(JavaMailSender mailSender,
                       CustomerRepository customerRepository, CareTakerRepository careTakerRepository,
                       AdminRepository adminRepository, BookingService bookingService) {
        this.mailSender = mailSender;
        this.customerRepository = customerRepository;
        this.careTakerRepository = careTakerRepository;
        this.adminRepository = adminRepository;
        this.bookingService = bookingService;
    }
    
    // Email template methods
    private String getBookingConfirmationTemplate(String customerName, String caretakerName, String bookingDate) {
        return "Dear " + customerName + ",\n\n" +
               "Your booking with " + caretakerName + " on " + bookingDate + " has been confirmed.\n\n" +
               "You can check your booking details in the CareNow app.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    private String getBookingCompletedTemplate(String customerName, String caretakerName, String bookingDate) {
        return "Dear " + customerName + ",\n\n" +
               "Your booking with " + caretakerName + " on " + bookingDate + " has been completed.\n\n" +
               "We hope you were satisfied with the service. Please consider leaving feedback.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    private String getBookingCanceledTemplate(String customerName, String caretakerName, String bookingDate) {
        return "Dear " + customerName + ",\n\n" +
               "We regret to inform you that your booking with " + caretakerName + " on " + bookingDate + " has been canceled.\n\n" +
               "We understand this may be disappointing and sincerely apologize for any inconvenience this may cause. Please know that we are here to assist you in rescheduling or finding an alternative caretaker at your earliest convenience.\n\n" +
               "Thank you for your understanding and patience.\n\n" +
               "Warm regards,\n" +
               "The CareNow Team";
    }    

    private String getNewBookingTemplate(String caretakerName, String customerName, String bookingDate) {
        return "Dear " + caretakerName + ",\n\n" +
               "You have received a new booking request from " + customerName + " for " + bookingDate + ".\n\n" +
               "Please log in to the CareNow app to view and accept/decline.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }

    private String getNewMessageTemplate(String receiverName, String senderName) {
        return "Dear " + receiverName + ",\n\n" +
               "You have received a new message from " + senderName + ".\n\n" +
               "Please log in to the CareNow app to view and respond.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    private String getCaretakerRegistrationTemplate(String caretakerName, String caretakerEmail, String caretakerPhone) {
        return "Dear Admin,\n\n" +
               "A new caretaker has registered on the CareNow platform.\n\n" +
               "Caretaker details:\n" +
               "Name: " + caretakerName + "\n" +
               "Email: " + caretakerEmail + "\n" +
               "Phone: " + caretakerPhone + "\n\n" +
               "Please review the information and approve or reject this registration.\n\n" +
               "Best regards,\n" +
               "The CareNow System";
    }
    
    private String getBookingPendingTemplate(String customerName, String caretakerName, String bookingDate) {
        return "Dear " + customerName + ",\n\n" +
               "Your booking request with " + caretakerName + " on " + bookingDate + " has been submitted and is pending.\n\n" +
               "The caretaker will review your request soon. You'll receive another notification when they accept or reject it.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    private String getCaretakerBookingCompletedTemplate(String caretakerName, String customerName, String bookingDate) {
        return "Dear " + caretakerName + ",\n\n" +
               "Your booking with " + customerName + " on " + bookingDate + " has been completed.\n\n" +
               "Thank you for providing your care services. The customer may leave feedback about your service.\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    private String getCaretakerApprovalTemplate(String caretakerName) {
        return "Dear " + caretakerName + ",\n\n" +
               "We are pleased to inform you that your CareNow care taker registration has been approved!\n\n" +
               "You can now log in to your account and start receiving booking requests from customers.\n\n" +
               "Here's what you can do next:\n" +
               "1. Complete your profile details\n" +
               "2. Set up your availability calendar\n" +
               "3. Wait for booking requests from customers\n\n" +
               "If you need any assistance, please contact our support team.\n\n" +
               "Welcome to the CareNow community!\n\n" +
               "Best regards,\n" +
               "The CareNow Team";
    }
    
    public void sendCaretakerApprovalNotificationById(Long accountId) {
        Long caretakerId = careTakerRepository.findCaretakerIdByAccountId(accountId);
        sendCaretakerApprovalNotificationForm(caretakerId);
    }

    public void sendCaretakerApprovalNotificationForm(Long careTakerId) {
        String caretakerEmail = careTakerRepository.findEmailById(careTakerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        String text = getCaretakerApprovalTemplate(caretakerName);
        sendEmail(caretakerEmail, "CareNow: Caretaker Approval", text);
    }

    // Helper methods for sending specific email types
    private void sendBookingConfirmation(String to, String customerName, String caretakerName, String bookingDate) {
        String subject = "CareNow: Booking Confirmation";
        String text = getBookingConfirmationTemplate(customerName, caretakerName, bookingDate);
        sendEmail(to, subject, text);
    }
    
    private void sendBookingCompleted(String to, String customerName, String caretakerName, String bookingDate) {
        String subject = "CareNow: Booking Completed";
        String text = getBookingCompletedTemplate(customerName, caretakerName, bookingDate);
        sendEmail(to, subject, text);
    }
    
    private void sendBookingCanceled(String to, String customerName, String caretakerName, String bookingDate) {
        String subject = "CareNow: Booking Canceled";
        String text = getBookingCanceledTemplate(customerName, caretakerName, bookingDate);
        sendEmail(to, subject, text);
    }
    
    private void sendNewMessageNotification(String to, String receiverName, String senderName) {
        String subject = "CareNow: New Message Received";
        String text = getNewMessageTemplate(receiverName, senderName);
        sendEmail(to, subject, text);
    }
    
    private void sendNewBookingNotification(String to, String caretakerName, String customerName, String bookingDate) {
        String subject = "CareNow: New Booking Request";
        String text = getNewBookingTemplate(caretakerName, customerName, bookingDate);
        sendEmail(to, subject, text);
    }
    
    private void sendBookingPendingNotification(String to, String customerName, String caretakerName, String bookingDate) {
        String subject = "CareNow: Booking Request Pending";
        String text = getBookingPendingTemplate(customerName, caretakerName, bookingDate);
        sendEmail(to, subject, text);
    }
    
    public void sendCaretakerRegistrationNotification(String caretakerName, String caretakerEmail, String caretakerPhone) {
        String subject = "CareNow: New Caretaker Registration";
        String text = getCaretakerRegistrationTemplate(caretakerName, caretakerEmail, caretakerPhone);
        sendEmailToAllAdmins(subject, text);
    }
    
    public void sendEmailToAllAdmins(String subject, String text) {
        List<String> adminEmails = getAdminEmails();
        
        for (String adminEmail : adminEmails) {
            sendEmail(adminEmail, subject, text);
        }
    }
    
    private List<String> getAdminEmails() {
        return adminRepository.findAllAdminEmails();
    }
    
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    // Methods that use IDs
    public void sendBookingConfirmationById(Long customerId, Long careTakerId, String bookingDate) {
        String customerEmail = customerRepository.findEmailById(customerId);
        String customerName = customerRepository.findNameById(customerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        
        if (customerEmail != null && customerName != null && caretakerName != null) {
            sendBookingConfirmation(customerEmail, customerName, caretakerName, bookingDate);
        }
    }
    
    public void sendBookingCompletedById(Long customerId, Long careTakerId, String bookingDate) {
        String customerEmail = customerRepository.findEmailById(customerId);
        String customerName = customerRepository.findNameById(customerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        
        if (customerEmail != null && customerName != null && caretakerName != null) {
            sendBookingCompleted(customerEmail, customerName, caretakerName, bookingDate);
        }
    }
    
    public void sendBookingCanceledById(Long customerId, Long careTakerId, String bookingDate) {
        String customerEmail = customerRepository.findEmailById(customerId);
        String customerName = customerRepository.findNameById(customerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        
        if (customerEmail != null && customerName != null && caretakerName != null) {
            sendBookingCanceled(customerEmail, customerName, caretakerName, bookingDate);
        }
    }
    
    public void sendNewMessageNotificationById(Long receiverId, String receiverType, Long senderId, String senderType) {
        String receiverEmail;
        String receiverName;
        String senderName;
        
        if ("CUSTOMER".equals(receiverType)) {
            receiverEmail = customerRepository.findEmailById(receiverId);
            receiverName = customerRepository.findNameById(receiverId);
        } else {
            receiverEmail = careTakerRepository.findEmailById(receiverId);
            receiverName = careTakerRepository.findNameById(receiverId);
        }
        
        if ("CUSTOMER".equals(senderType)) {
            senderName = customerRepository.findNameById(senderId);
        } else {
            senderName = careTakerRepository.findNameById(senderId);
        }
        
        if (receiverEmail != null && receiverName != null && senderName != null) {
            sendNewMessageNotification(receiverEmail, receiverName, senderName);
        }
    }
    
    public void sendNewBookingNotificationById(Long careTakerId, Long customerId, String bookingDate) {
        String caretakerEmail = careTakerRepository.findEmailById(careTakerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        String customerName = customerRepository.findNameById(customerId);
        
        if (caretakerEmail != null && caretakerName != null && customerName != null) {
            sendNewBookingNotification(caretakerEmail, caretakerName, customerName, bookingDate);
        }
    }
    
    public void sendBookingPendingById(Long customerId, Long careTakerId, String bookingDate) {
        String customerEmail = customerRepository.findEmailById(customerId);
        String customerName = customerRepository.findNameById(customerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        
        if (customerEmail != null && customerName != null && caretakerName != null) {
            sendBookingPendingNotification(customerEmail, customerName, caretakerName, bookingDate);
        }
    }
    
    private void sendCaretakerBookingCompleted(String to, String caretakerName, String customerName, String bookingDate) {
        String subject = "CareNow: Booking Completed";
        String text = getCaretakerBookingCompletedTemplate(caretakerName, customerName, bookingDate);
        sendEmail(to, subject, text);
    }

    public void sendCaretakerBookingCompletedById(Long careTakerId, Long customerId, String bookingDate) {
        String caretakerEmail = careTakerRepository.findEmailById(careTakerId);
        String caretakerName = careTakerRepository.findNameById(careTakerId);
        String customerName = customerRepository.findNameById(customerId);
        
        if (caretakerEmail != null && caretakerName != null && customerName != null) {
            sendCaretakerBookingCompleted(caretakerEmail, caretakerName, customerName, bookingDate);
        }
    }
    
    public void sendBookingStatusEmail(Long bookingId, String status) {
        // Get booking details
        Booking booking = bookingService.getBookingEntityById(bookingId);
        Long customerId = booking.getCustomer().getCustomer_id();
        Long careTakerId = booking.getCare_taker().getCare_taker_id();
        String bookingDate = booking.getCreatedAt().toString();
        
        // Send appropriate email based on status
        String statusUpper = status.toUpperCase();
        
        switch (statusUpper) {
            case "COMPLETED":
                sendBookingCompletedById(customerId, careTakerId, bookingDate);
                sendCaretakerBookingCompletedById(careTakerId, customerId, bookingDate);
                break;
            case "ACCEPT":
                sendBookingConfirmationById(customerId, careTakerId, bookingDate);
                break;
            case "REJECT":
                sendBookingCanceledById(customerId, careTakerId, bookingDate);
                break;
            case "PENDING":
                // Send notification to caretaker
                sendNewBookingNotificationById(careTakerId, customerId, bookingDate);
                // Send notification to customer
                sendBookingPendingById(customerId, careTakerId, bookingDate);
                break;
            default:
                throw new IllegalArgumentException("Unknown booking status: " + statusUpper);
        }
    }

}

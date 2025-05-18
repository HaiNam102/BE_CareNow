package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.ChatMessageRequest;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.ChatMessageResponse;
import com.example.Cap2.NannyNow.Entity.ChatRoom;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.ChatService;
import com.example.Cap2.NannyNow.Service.AccountService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import com.example.Cap2.NannyNow.DTO.Response.Chat.ChatRoomCreatedResponse;
import com.example.Cap2.NannyNow.DTO.Response.Chat.ChatMessageSimpleResponse;
import com.example.Cap2.NannyNow.Service.EmailService;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;
    private final AccountService accountService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
        System.out.println("==== WebSocket message received: " + chatMessageRequest + " ====");
        try {
            // Save the message to the database
            ChatMessageResponse response = chatService.saveAndGetMessage(chatMessageRequest);
            System.out.println("Message saved to database: " + response);
            
            // Broadcast the message
            String destination = "/topic/room." + chatMessageRequest.getRoomId();
            System.out.println("Broadcasting to: " + destination);
            
            messagingTemplate.convertAndSend(destination, response);
            System.out.println("Message broadcast complete");
            
            // Send email notification to the recipient
            try {
                // Get recipient ID and type
                Long senderId = chatMessageRequest.getSenderId();
                String senderType = chatMessageRequest.getSenderType();
                
                // Determine recipient based on room and sender
                ChatRoom chatRoom = chatService.getChatRoomById(chatMessageRequest.getRoomId());
                Long recipientId;
                String recipientType;
                
                if ("CUSTOMER".equals(senderType)) {
                    recipientId = chatRoom.getCare_taker().getCare_taker_id();
                    recipientType = "CARE_TAKER";
                } else {
                    recipientId = chatRoom.getCustomer().getCustomer_id();
                    recipientType = "CUSTOMER";
                }
                
                // Send email notification
                emailService.sendNewMessageNotificationById(recipientId, recipientType, senderId, senderType);
            } catch (Exception e) {
                // Log the error but don't fail the message sending
                System.err.println("Failed to send email notification: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("==== ERROR in WebSocket message processing ====");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        List<ChatMessageResponse> messages = chatService.getRoomMessages(roomId);
        List<ChatMessageSimpleResponse> simpleMessages = messages.stream()
            .map(msg -> new ChatMessageSimpleResponse(
                msg.getContent(),
                msg.getSenderType(),
                msg.getSenderName(),
                msg.getCreatedAt() instanceof LocalDateTime ? 
                    (LocalDateTime)msg.getCreatedAt() : LocalDateTime.now()
            ))
            .toList();
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(simpleMessages)
                .build()
        );
    }

    /**
     * @deprecated Use /rooms/customer/{customerId} or /rooms/caretaker/{caretakerId} instead
     */
    @Deprecated
    @GetMapping("/rooms")
    public ResponseEntity<?> getUserChatRooms(
            @RequestParam("userId") Long userId,
            @RequestParam("userType") String userType) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRoomsDTO(userId, userType))
                .build()
        );
    }

    @GetMapping("/rooms/customer/{customerId}")
    public ResponseEntity<?> getCustomerChatRooms(@PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRoomsDTO(customerId, "CUSTOMER"))
                .build()
        );
    }

    @GetMapping("/rooms/caretaker/{caretakerId}")
    public ResponseEntity<?> getCareTakerChatRooms(@PathVariable Long caretakerId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRoomsDTO(caretakerId, "CARE_TAKER"))
                .build()
        );
    }

    /**
     * Get chat rooms for a customer by username
     */
    @GetMapping("/rooms/customer/customerId/{customerId}")
    public ResponseEntity<?> getCustomerChatRoomsById(@PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRoomsDTO(customerId, "CUSTOMER"))
                .build()
        );
    }
    
    /**
     * Get chat rooms for a care taker by username
     */
    @GetMapping("/rooms/caretaker/caretakerId/{caretakerId}")
    public ResponseEntity<?> getCareTakerChatRoomsById(@PathVariable Long caretakerId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRoomsDTO(caretakerId, "CARE_TAKER"))
                .build()
        );
    }

    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(
            @RequestParam("customerId") Long customerId,
            @RequestParam("careTakerId") Long careTakerId) {
        ChatRoom room = chatService.createChatRoom(customerId, careTakerId);
        ChatRoomCreatedResponse response = new ChatRoomCreatedResponse(
            room.getRoomId(), 
            customerId, 
            careTakerId,
            room.getCreatedAt() instanceof LocalDateTime ? 
                (LocalDateTime)room.getCreatedAt() : LocalDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(response)
                .build()
        );
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getChatRoomDetails(
            @PathVariable Long roomId,
            @RequestParam("userId") Long userId,
            @RequestParam("userType") String userType) {
        
        // Verify the user has access to this room
        ChatRoom room = chatService.getChatRoomById(roomId);
        
        // Check if user has access to this room
        boolean hasAccess = false;
        if ("CUSTOMER".equals(userType) && room.getCustomer().getCustomer_id().equals(userId)) {
            hasAccess = true;
        } else if ("CARE_TAKER".equals(userType) && room.getCare_taker().getCare_taker_id().equals(userId)) {
            hasAccess = true;
        }
        
        if (!hasAccess) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(
                ApiResponse.builder()
                    .code(403) // Changed from "403" to 403
                    .message("You don't have access to this chat room")
                    .build()
            );
        }
        
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(room)
                .build()
        );
    }
    @PostMapping("/test-save")
    public ResponseEntity<?> testSaveMessage(@RequestBody ChatMessageRequest request) {
    try {
        ChatMessageResponse response = chatService.saveAndGetMessage(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(response)
                .build());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(9999)
                .message("Error: " + e.getMessage())
                .build());
    }
}
}





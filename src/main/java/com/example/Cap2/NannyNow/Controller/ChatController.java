package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Request.ChatMessageRequest;
import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.DTO.Response.ChatMessageResponse;
import com.example.Cap2.NannyNow.Entity.ChatRoom;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Service.ChatService;
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

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

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
        } catch (Exception e) {
            System.err.println("==== ERROR in WebSocket message processing ====");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        List<ChatMessageResponse> messages = chatService.getRoomMessages(roomId);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(messages)
                .build()
        );
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getUserChatRooms(
            @RequestParam("userId") Long userId,
            @RequestParam("userType") String userType) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(chatService.getUserChatRooms(userId, userType))
                .build()
        );
    }

    @GetMapping("/partners")
    public ResponseEntity<?> getChatPartners(
        @RequestParam("userId") Long userId,
        @RequestParam("userType") String userType) {
        return ResponseEntity.ok(ApiResponse.builder()
            .code(SuccessCode.GET_SUCCESSFUL.getCode())
            .message(SuccessCode.GET_SUCCESSFUL.getMessage())
            .data(chatService.getChatPartnersWithLastMessage(userId, userType))
            .build()
        );
    }

    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(
            @RequestParam("customerId") Long customerId,
            @RequestParam("careTakerId") Long careTakerId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.ADD_SUCCESSFUL.getCode())
                .message(SuccessCode.ADD_SUCCESSFUL.getMessage())
                .data(chatService.createChatRoom(customerId, careTakerId))
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





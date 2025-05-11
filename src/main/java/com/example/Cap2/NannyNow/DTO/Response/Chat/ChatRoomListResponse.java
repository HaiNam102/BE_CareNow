package com.example.Cap2.NannyNow.DTO.Response.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListResponse {
    private Long roomId;
    
    // Partner info (the person user is chatting with)
    private Long partnerId;
    private String partnerName;
    private String partnerType; // "CUSTOMER" or "CARE_TAKER"
    
    // Room info
    private LocalDateTime createdAt;
    
    // Last message info (optional)
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Long lastMessageSenderId;
    private String lastMessageSenderType;
    
    // Helper for UI to know if the last message is unread
    private boolean hasUnreadMessages;
}

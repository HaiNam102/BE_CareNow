package com.example.Cap2.NannyNow.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    Long messageId;
    Long roomId;
    Long senderId;
    String senderName;
    String senderType;
    String content;
    boolean isRead;
    LocalDateTime createdAt;
}
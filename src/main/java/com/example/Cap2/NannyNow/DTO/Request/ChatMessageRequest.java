package com.example.Cap2.NannyNow.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRequest {
    Long roomId;
    Long senderId;
    String senderType;
    String content;
}
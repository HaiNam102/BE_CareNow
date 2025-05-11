package com.example.Cap2.NannyNow.DTO.Response.Chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageSimpleResponse {
    private String content;
    private String senderType;
    private String senderName;
    private LocalDateTime createdAt;
} 
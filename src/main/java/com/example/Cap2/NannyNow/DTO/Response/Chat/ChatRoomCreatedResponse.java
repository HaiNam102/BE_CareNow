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
public class ChatRoomCreatedResponse {
    private Long roomId;
    private Long customerId;
    private Long careTakerId;
    private LocalDateTime createdAt;
}
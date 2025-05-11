package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long messageId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    ChatRoom chatRoom;

    @Column(name = "sender_id")
    Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type")
    SenderType senderType;

    @Column(name = "content")
    String content;

    @Column(name = "is_read")
    boolean isRead = false;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    public enum SenderType {
        CUSTOMER,
        CARE_TAKER
    }
}
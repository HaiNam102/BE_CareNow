package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomRoomIdOrderByCreatedAtAsc(Long roomId);
    
    Optional<ChatMessage> findFirstByChatRoomRoomIdOrderByCreatedAtAsc(Long roomId);
    
    long countByChatRoomRoomIdAndSenderIdNotAndIsReadFalse(Long roomId, Long userId);
}
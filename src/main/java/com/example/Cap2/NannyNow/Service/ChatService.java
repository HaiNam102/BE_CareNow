package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.ChatMessageRequest;
import com.example.Cap2.NannyNow.DTO.Response.ChatMessageResponse;
import com.example.Cap2.NannyNow.DTO.Response.Chat.ChatRoomListResponse;
import com.example.Cap2.NannyNow.Entity.*;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Repository.*;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {
    ChatRoomRepository chatRoomRepository;
    ChatMessageRepository chatMessageRepository;
    CustomerRepository customerRepository;
    CareTakerRepository careTakerRepository;

    @Transactional
    public ChatMessageResponse saveAndGetMessage(ChatMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        
        ChatMessage message = new ChatMessage();
        message.setChatRoom(room);
        message.setSenderId(request.getSenderId());
        message.setSenderType(ChatMessage.SenderType.valueOf(request.getSenderType()));
        message.setContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return convertToResponse(savedMessage);
    }

    public List<Object> getChatPartners(Long userId, String userType) {
        List<ChatRoom> rooms = getUserChatRooms(userId, userType);
        
        if ("CUSTOMER".equals(userType)) {
            // Return list of care takers this customer has chatted with
            return rooms.stream()
                    .map(ChatRoom::getCare_taker)
                    .collect(Collectors.toList());
        } else if ("CARE_TAKER".equals(userType)) {
            // Return list of customers this care taker has chatted with
            return rooms.stream()
                    .map(ChatRoom::getCustomer)
                    .collect(Collectors.toList());
        }
        
        throw new ApiException(ErrorCode.INVALID_USER_TYPE);
    }
    
    // More detailed version with chat info
    public List<Map<String, Object>> getChatPartnersWithLastMessage(Long userId, String userType) {
        List<ChatRoom> rooms = getUserChatRooms(userId, userType);
        
        List<Map<String, Object>> resultList = rooms.stream().map(room -> {
            Map<String, Object> result = new HashMap<>();
            
            // Get partner info
            if ("CUSTOMER".equals(userType)) {
                CareTaker partner = room.getCare_taker();
                result.put("partnerId", partner.getCare_taker_id());
                result.put("partnerName", partner.getNameOfCareTaker());
                result.put("partnerType", "CARE_TAKER");
            } else {
                Customer partner = room.getCustomer();
                result.put("partnerId", partner.getCustomer_id());
                result.put("partnerName", partner.getNameOfCustomer());
                result.put("partnerType", "CUSTOMER");
            }
            
            // Get room info
            result.put("roomId", room.getRoomId());
            Object createdAt = room.getCreatedAt();
            if (createdAt instanceof LocalDateTime dt) {
                result.put("createdAt", dt);
            }
            
            // Get last message if exists
            chatMessageRepository.findFirstByChatRoomRoomIdOrderByCreatedAtDesc(room.getRoomId())
                .ifPresent(lastMessage -> {
                    result.put("lastMessage", lastMessage.getContent());
                    result.put("lastMessageTime", lastMessage.getCreatedAt());
                    result.put("lastMessageSenderId", lastMessage.getSenderId());
                    result.put("lastMessageSenderType", lastMessage.getSenderType());
                });
            
            return result;
        }).toList();
        
        // Sort by lastMessageTime in descending order (newest first)
        // If lastMessageTime is null, use createdAt as fallback
        resultList.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.getOrDefault("lastMessageTime", a.get("createdAt"));
            LocalDateTime timeB = (LocalDateTime) b.getOrDefault("lastMessageTime", b.get("createdAt"));
            
            // Handle null values (rooms with no messages)
            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;
            
            // Sort in descending order (newest first)
            return timeB.compareTo(timeA);
        });
        
        return resultList;
    }
    
    public ChatRoom createChatRoom(Long customerId, Long careTakerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        CareTaker careTaker = careTakerRepository.findById(careTakerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        // Check if room already exists
        ChatRoom existingRoom = chatRoomRepository
                .findByCustomerAndCareTaker(customer, careTaker)
                .orElse(null);
        
        if (existingRoom != null) {
            return existingRoom;
        }

        ChatRoom newRoom = new ChatRoom();
        newRoom.setCustomer(customer);
        newRoom.setCare_taker(careTaker);
        newRoom.setCreatedAt(LocalDateTime.now());
        newRoom.setUpdatedAt(LocalDateTime.now());
        
        return chatRoomRepository.save(newRoom);
    }

    public List<ChatRoom> getUserChatRooms(Long userId, String userType) {
        if ("CUSTOMER".equals(userType)) {
            return chatRoomRepository.findByCustomerId(userId);
        } else if ("CARE_TAKER".equals(userType)) {
            return chatRoomRepository.findByCareTakerId(userId);
        }
        throw new ApiException(ErrorCode.INVALID_USER_TYPE);
    }

    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }

    public List<ChatMessageResponse> getRoomMessages(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomRoomIdOrderByCreatedAtAsc(roomId);
        return messages.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ChatMessageResponse convertToResponse(ChatMessage message) {
        String senderName = getSenderName(message.getSenderId(), message.getSenderType());
        
        return new ChatMessageResponse(
            message.getMessageId(),
            message.getChatRoom().getRoomId(),
            message.getSenderId(),
            senderName,
            message.getSenderType().toString(),
            message.getContent(),
            message.isRead(),
            message.getCreatedAt()
        );
    }

    private String getSenderName(Long senderId, ChatMessage.SenderType senderType) {
        if (senderType == ChatMessage.SenderType.CUSTOMER) {
            return customerRepository.findById(senderId)
                    .map(Customer::getNameOfCustomer)
                    .orElse("Unknown Customer");
        } else if (senderType == ChatMessage.SenderType.CARE_TAKER) {
            return careTakerRepository.findById(senderId)
                    .map(CareTaker::getNameOfCareTaker)
                    .orElse("Unknown CareTaker");
        }
        return "System";
        
    }

    public List<ChatRoomListResponse> getUserChatRoomsDTO(Long userId, String userType) {
        List<ChatRoom> rooms = getUserChatRooms(userId, userType);
        
        List<ChatRoomListResponse> responseList = rooms.stream().map(room -> {
            ChatRoomListResponse.ChatRoomListResponseBuilder builder = ChatRoomListResponse.builder()
                    .roomId(room.getRoomId());
            
            // Set partner info based on user type
            if ("CUSTOMER".equals(userType)) {
                // For customer, partner is the caretaker
                CareTaker partner = room.getCare_taker();
                builder.partnerId(partner.getCare_taker_id())
                      .partnerName(partner.getNameOfCareTaker())
                      .partnerType("CARE_TAKER");
            } else {
                // For caretaker, partner is the customer
                Customer partner = room.getCustomer();
                builder.partnerId(partner.getCustomer_id())
                      .partnerName(partner.getNameOfCustomer())
                      .partnerType("CUSTOMER");
            }
            
            // Handle created date
            Object createdAt = room.getCreatedAt();
            if (createdAt instanceof LocalDateTime dt) {
                builder.createdAt(dt);
            } else if (createdAt instanceof Object[] arr) {
                // Handle array format [year, month, day, hour, minute, second]
                int[] dateArr = new int[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    dateArr[i] = (int)arr[i];
                }
                builder.createdAt(LocalDateTime.of(
                    dateArr[0], dateArr[1], dateArr[2], 
                    dateArr[3], dateArr[4], dateArr[5]
                ));
            }
            
            // Get last message if exists
            chatMessageRepository.findFirstByChatRoomRoomIdOrderByCreatedAtDesc(room.getRoomId())
                .ifPresent(message -> {
                    builder.lastMessage(message.getContent())
                           .lastMessageSenderId(message.getSenderId())
                           .lastMessageSenderType(message.getSenderType().toString());
                    
                    // Handle message date
                    if (message.getCreatedAt() instanceof LocalDateTime) {
                        builder.lastMessageTime((LocalDateTime)message.getCreatedAt());
                    }
                    
                    // Check if there are unread messages for this user
                    boolean hasUnread = chatMessageRepository
                        .countByChatRoomRoomIdAndSenderIdNotAndIsReadFalse(
                            room.getRoomId(), userId) > 0;
                    builder.hasUnreadMessages(hasUnread);
                });
            
            return builder.build();
        }).collect(Collectors.toList());
        
        // Sort by lastMessageTime in descending order (newest first)
        // If lastMessageTime is null, use createdAt as fallback
        responseList.sort((a, b) -> {
            LocalDateTime timeA = a.getLastMessageTime() != null ? a.getLastMessageTime() : a.getCreatedAt();
            LocalDateTime timeB = b.getLastMessageTime() != null ? b.getLastMessageTime() : b.getCreatedAt();
            
            // Handle null values (rooms with no messages)
            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;
            
            // Sort in descending order (newest first)
            return timeB.compareTo(timeA);
        });
        
        return responseList;
    }

}



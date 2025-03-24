package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.CareRecipientReq;
import com.example.Cap2.NannyNow.Entity.CareRecipient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CareRecipientMapper {
    CareRecipientMapper INSTANCE = Mappers.getMapper(CareRecipientMapper.class);
    
    CareRecipient toCareRecipient(CareRecipientReq careRecipientReq);
} 
package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.OptionsDetailsReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionsDetailsRes;
import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionsDetailsMapper {
    OptionsDetailsMapper INSTANCE = Mappers.getMapper(OptionsDetailsMapper.class);
    
    OptionsDetails toOptionsDetails(OptionsDetailsReq optionsDetailsReq);
    OptionsDetailsRes toOptionsDetailsRes(OptionsDetails optionsDetails);
}


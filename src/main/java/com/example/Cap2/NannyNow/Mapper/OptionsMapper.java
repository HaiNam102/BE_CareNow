package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.OptionsReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionsRes;
import com.example.Cap2.NannyNow.Entity.Options;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionsMapper {
    OptionsMapper INSTANCE = Mappers.getMapper(OptionsMapper.class);
    
    Options toOptions(OptionsReq optionsReq);
    OptionsRes toOptionsRes(Options options);
}

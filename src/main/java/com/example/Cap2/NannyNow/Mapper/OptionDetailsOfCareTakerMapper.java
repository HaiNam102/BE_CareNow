package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.OptionDetailsOfCareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionDetailsOfCareTakerRes;
import com.example.Cap2.NannyNow.Entity.OptionDetailsOfCareTaker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionDetailsOfCareTakerMapper {
    OptionDetailsOfCareTakerMapper INSTANCE = Mappers.getMapper(OptionDetailsOfCareTakerMapper.class);
    
    OptionDetailsOfCareTaker toOptionDetailsOfCareTaker(OptionDetailsOfCareTakerReq req);

    @Mapping(target = "optionDetailsId", expression = "java(entity.getOptionsDetails() != null ? entity.getOptionsDetails().getOptionDetailsId() : null)")
    @Mapping(target = "careTakerId", expression = "java(entity.getCare_taker() != null ? entity.getCare_taker().getCare_taker_id() : null)")
    OptionDetailsOfCareTakerRes toOptionDetailsOfCareTakerRes(OptionDetailsOfCareTaker entity);
}



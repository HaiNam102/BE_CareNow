package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.OptionDetailsOfCareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionDetailsOfCareTakerRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.OptionDetailsOfCareTaker;
import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.OptionDetailsOfCareTakerMapper;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.OptionDetailsOfCareTakerRepository;
import com.example.Cap2.NannyNow.Repository.OptionsDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionDetailsOfCareTakerService {
    OptionDetailsOfCareTakerMapper mapper;
    OptionDetailsOfCareTakerRepository repository;
    OptionsDetailsRepository optionsDetailsRepository;
    CareTakerRepository careTakerRepository;

    public List<OptionDetailsOfCareTakerRes> getAllOptionDetailsOfCareTaker() {
        return repository.findAll().stream()
                .map(mapper::toOptionDetailsOfCareTakerRes)
                .collect(Collectors.toList());
    }

    public OptionDetailsOfCareTakerRes getOptionDetailsOfCareTakerById(Long id) {
        OptionDetailsOfCareTaker entity = repository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_OF_CARETAKER_NOT_FOUND));
        return mapper.toOptionDetailsOfCareTakerRes(entity);
    }

    @Transactional
    public List<OptionDetailsOfCareTakerRes> createOptionDetailsOfCareTaker(
            List<OptionDetailsOfCareTakerReq> requests) {
        List<OptionDetailsOfCareTakerRes> responses = new ArrayList<>();
        
        for (OptionDetailsOfCareTakerReq request : requests) {
            OptionsDetails optionsDetails = optionsDetailsRepository.findById(request.getOptionDetailsId())
                    .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));
            
            CareTaker careTaker = careTakerRepository.findById(request.getCareTakerId())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            
            OptionDetailsOfCareTaker entity = mapper.toOptionDetailsOfCareTaker(request);
            entity.setOptionsDetails(optionsDetails);
            entity.setCare_taker(careTaker);
            
            OptionDetailsOfCareTaker savedEntity = repository.save(entity);
            responses.add(mapper.toOptionDetailsOfCareTakerRes(savedEntity));
        }
        
        return responses;
    }

    public OptionDetailsOfCareTakerRes updateOptionDetailsOfCareTaker(Long id, OptionDetailsOfCareTakerReq req) {
        OptionDetailsOfCareTaker existingEntity = repository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_OF_CARETAKER_NOT_FOUND));
        
        OptionsDetails optionsDetails = optionsDetailsRepository.findById(req.getOptionDetailsId())
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));
        
        CareTaker careTaker = careTakerRepository.findById(req.getCareTakerId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        OptionDetailsOfCareTaker updatedEntity = mapper.toOptionDetailsOfCareTaker(req);
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setOptionsDetails(optionsDetails);
        updatedEntity.setCare_taker(careTaker);
        OptionDetailsOfCareTaker savedEntity = repository.save(updatedEntity);
        return mapper.toOptionDetailsOfCareTakerRes(savedEntity);
    }

    public void deleteOptionDetailsOfCareTaker(Long id) {
        if (!repository.existsById(id)) {
            throw new ApiException(ErrorCode.OPTION_DETAIL_OF_CARETAKER_NOT_FOUND);
        }
        repository.deleteById(id);
    }
}









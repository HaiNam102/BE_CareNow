package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.OptionsDetailsReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionsDetailsRes;
import com.example.Cap2.NannyNow.Entity.Options;
import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.OptionsDetailsMapper;
import com.example.Cap2.NannyNow.Repository.OptionsDetailsRepository;
import com.example.Cap2.NannyNow.Repository.OptionsRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionsDetailsService {
    OptionsDetailsMapper optionsDetailsMapper;
    OptionsDetailsRepository optionsDetailsRepository;
    OptionsRepository optionsRepository;

    public List<OptionsDetailsRes> getAllOptionsDetails() {
        List<OptionsDetails> optionsDetails = optionsDetailsRepository.findAllWithOptions();
        return optionsDetails.stream()
                .map(detail -> {
                    OptionsDetailsRes res = optionsDetailsMapper.toOptionsDetailsRes(detail);
                    if (detail.getOptions() != null) {
                        res.setOptionsId(detail.getOptions().getOptionsId());
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    public OptionsDetailsRes getOptionsDetailsById(Long id) {
        OptionsDetails optionsDetails = optionsDetailsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));
        
        OptionsDetailsRes res = optionsDetailsMapper.toOptionsDetailsRes(optionsDetails);
        if (optionsDetails.getOptions() != null) {
            res.setOptionsId(optionsDetails.getOptions().getOptionsId());
        }
        return res;
    }

    @Transactional
    public OptionsDetailsRes createOptionsDetails(OptionsDetailsReq optionsDetailsReq) {
        Options options = optionsRepository.findById(optionsDetailsReq.getOptionsId())
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_NOT_FOUND));

        OptionsDetails optionsDetails = optionsDetailsMapper.toOptionsDetails(optionsDetailsReq);
        optionsDetails.setOptions(options);

        OptionsDetails savedOptionsDetails = optionsDetailsRepository.save(optionsDetails);
        
        OptionsDetailsRes res = optionsDetailsMapper.toOptionsDetailsRes(savedOptionsDetails);
        res.setOptionsId(options.getOptionsId());
        return res;
    }

    @Transactional
    public OptionsDetailsRes updateOptionsDetails(Long id, OptionsDetailsReq optionsDetailsReq) {
        OptionsDetails existingOptionsDetails = optionsDetailsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));

        Options options = optionsRepository.findById(optionsDetailsReq.getOptionsId())
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_NOT_FOUND));

        existingOptionsDetails.setDetailName(optionsDetailsReq.getDetailName());
        existingOptionsDetails.setOptions(options);

        OptionsDetails updatedOptionsDetails = optionsDetailsRepository.save(existingOptionsDetails);
        
        OptionsDetailsRes res = optionsDetailsMapper.toOptionsDetailsRes(updatedOptionsDetails);
        res.setOptionsId(options.getOptionsId());
        return res;
    }

    @Transactional
    public void deleteOptionsDetails(Long id) {
        if (!optionsDetailsRepository.existsById(id)) {
            throw new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND);
        }
        optionsDetailsRepository.deleteById(id);
    }

    public List<OptionsDetailsRes> getOptionsDetailsByOptionsId(Long optionsId) {
        Options options = optionsRepository.findById(optionsId)
                .orElseThrow(() -> new ApiException(ErrorCode.OPTION_NOT_FOUND));

        List<OptionsDetails> optionsDetails = optionsDetailsRepository.findAllWithOptions().stream()
                .filter(detail -> detail.getOptions() != null && 
                        detail.getOptions().getOptionsId().equals(optionsId))
                .collect(Collectors.toList());

        return optionsDetails.stream()
                .map(detail -> {
                    OptionsDetailsRes res = optionsDetailsMapper.toOptionsDetailsRes(detail);
                    res.setOptionsId(optionsId);
                    return res;
                })
                .collect(Collectors.toList());
    }
}



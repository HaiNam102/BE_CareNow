package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.OptionsReq;
import com.example.Cap2.NannyNow.DTO.Response.Options.OptionsRes;
import com.example.Cap2.NannyNow.Entity.Options;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.OptionsMapper;
import com.example.Cap2.NannyNow.Repository.OptionsRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OptionsService {
    OptionsMapper optionsMapper;
    OptionsRepository optionsRepository;

    public List<OptionsRes> getAllOptions() {
        List<Options> options = optionsRepository.findAll();
        List<OptionsRes> optionsRes = new ArrayList<>();
        for(Options option : options) {
            OptionsRes optionsRes1 = optionsMapper.toOptionsRes(option);
            optionsRes.add(optionsRes1);
        }
        return optionsRes;
    }

    public OptionsRes getOptionById(Long id) {
        Options options = optionsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return optionsMapper.toOptionsRes(options);
    }

    public OptionsRes createOption(OptionsReq optionsReq) {
        Options options = optionsMapper.toOptions(optionsReq);
        Options savedOptions = optionsRepository.save(options);
        return optionsMapper.toOptionsRes(savedOptions);
    }

    public OptionsRes updateOption(Long id, OptionsReq optionsReq) {
        Options existingOptions = optionsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Options updatedOptions = optionsMapper.toOptions(optionsReq);
        updatedOptions.setOptionsId(existingOptions.getOptionsId());
        Options savedOptions = optionsRepository.save(updatedOptions);
        return optionsMapper.toOptionsRes(savedOptions);
    }

    public void deleteOption(Long id) {
        if (!optionsRepository.existsById(id)) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        optionsRepository.deleteById(id);
    }
}

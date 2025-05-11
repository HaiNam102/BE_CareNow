package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CareRecipientReq;
import com.example.Cap2.NannyNow.DTO.Response.CareRecipientRes;
import com.example.Cap2.NannyNow.Entity.CareRecipient;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CareRecipientMapper;
import com.example.Cap2.NannyNow.Repository.CareRecipientRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareRecipientService {
    CareRecipientRepository careRecipientRepository;
    CareRecipientMapper careRecipientMapper;
    CustomerRepository customerRepository;

    public List<CareRecipientRes> getCareRecipientsByCustomerId(Long customerId){
        List<CareRecipient> careRecipients = careRecipientRepository.getCareRecipientsByCustomerId(customerId);
        return careRecipients.stream()
                .map(careRecipientMapper::toCareRecipientRes)
                .collect(Collectors.toList());
    }
    
    public CareRecipientRes createCareRecipient(CareRecipientReq careRecipientReq, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        
        CareRecipient careRecipient = careRecipientMapper.toCareRecipient(careRecipientReq);
        careRecipient.setCustomer(customer);
        
        CareRecipient savedCareRecipient = careRecipientRepository.save(careRecipient);
        return careRecipientMapper.toCareRecipientRes(savedCareRecipient);
    }
    
    public CareRecipientRes updateCareRecipient(Long careRecipientId, CareRecipientReq careRecipientReq) {
        CareRecipient existingCareRecipient = careRecipientRepository.findById(careRecipientId)
                .orElseThrow(() -> new ApiException(ErrorCode.CARE_RECIPIENT_NOT_FOUND));
        existingCareRecipient.setName(careRecipientReq.getName());
        existingCareRecipient.setGender(careRecipientReq.getGender());
        existingCareRecipient.setYearOld(careRecipientReq.getYearOld());
        existingCareRecipient.setSpecialDetail(careRecipientReq.getSpecialDetail());
        
        CareRecipient updatedCareRecipient = careRecipientRepository.save(existingCareRecipient);
        return careRecipientMapper.toCareRecipientRes(updatedCareRecipient);
    }
}

package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Response.CareRecipientRes;
import com.example.Cap2.NannyNow.Entity.CareRecipient;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Mapper.CareRecipientMapper;
import com.example.Cap2.NannyNow.Repository.CareRecipientRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareRecipientService {
    CareRecipientRepository careRecipientRepository;
    CareRecipientMapper careRecipientMapper;

    public CareRecipientRes getCareRecipientByCustomerId(Long customerId){
        CareRecipient careRecipient = careRecipientRepository.getCareRecipientByCustomerId(customerId);
        CareRecipientRes careRecipientRes = careRecipientMapper.toCareRecipientRes(careRecipient);
        return careRecipientRes;
    }
}

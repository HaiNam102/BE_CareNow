package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CustomerReq;
import com.example.Cap2.NannyNow.DTO.Response.CustomerRes;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CustomerMapper;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    CustomerRepository customerRepository;

    public List<CustomerRes> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerRes)
                .collect(Collectors.toList());
    }

    public CustomerRes getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return customerMapper.toCustomerRes(customer);
    }

    public CustomerReq createCustomer(CustomerReq customerReq) {
        Customer customer = customerMapper.toCustomer(customerReq);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerReq(savedCustomer);
    }

    public CustomerReq updateCustomer(Long id, CustomerReq customerReq) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Customer updatedCustomer = customerMapper.toCustomer(customerReq);
        updatedCustomer.setCustomer_id(existingCustomer.getCustomer_id());
        updatedCustomer.setAccount(existingCustomer.getAccount());
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toCustomerReq(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        customerRepository.deleteCustomerAndRelatedData(id);
    }

}
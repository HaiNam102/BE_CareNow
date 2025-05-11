package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.DTO.Request.CustomerReq;
import com.example.Cap2.NannyNow.DTO.Response.CustomerRes;
import com.example.Cap2.NannyNow.Entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    @Mapping(source = "nameOfUser" , target = "nameOfCustomer")
    Customer toCustomer(RegisterDTO registerDTO);
    Customer toCustomer(CustomerReq customerReq);

    CustomerRes toCustomerRes(Customer customer);

    CustomerReq toCustomerReq(Customer customer);
}

package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.Entity.*;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.AccountMapper;
import com.example.Cap2.NannyNow.Mapper.CareTakerMapper;
import com.example.Cap2.NannyNow.Mapper.CustomerMapper;
import com.example.Cap2.NannyNow.Repository.*;
import com.example.Cap2.NannyNow.Service.Cloud.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    CustomerRepository customerRepository;
    CareTakerRepository careTakerRepository;
    PasswordEncoder passwordEncoder;
    CustomerMapper customerMapper;
    CareTakerMapper careTakerMapper;
    AccountMapper accountMapper;
    ImageRepository imageRepository;
    OptionDetailsOfCareTakerRepository optionDetailsOfCareTakerRepository;
    OptionsDetailsRepository optionsDetailsRepository;
    CloudinaryService cloudinaryService;


    Account_RoleRepository account_RoleRepository;

    public Account getAccountByUserName(String userName){
        return this.accountRepository.findByUserName(userName);
    }

    @Transactional
    public RegisterDTO register(RegisterDTO registerDTO,MultipartFile imgProfile,MultipartFile imgCccd) throws IOException {
        Role role = roleRepository.findByRoleName(registerDTO.getRoleName());
        if(role == null ) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }
        if(accountRepository.existsByUsernameOrEmailOrPhoneNumber(registerDTO.getUserName(),registerDTO.getEmail(), registerDTO.getPhoneNumber())){
            throw new ApiException(ErrorCode.MAIL_PHONE_USERNAME_ALREADY_EXITS);
        }
        Account account = accountMapper.toAccount(registerDTO);
        account.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        accountRepository.save(account);

        Account_Role account_role = new Account_Role();
        account_role.setAccount(account);
        account_role.setRole(role);
        account_RoleRepository.save(account_role);

        if(role.getRoleName().equalsIgnoreCase("CUSTOMER")){
            Customer customer = customerMapper.toCustomer(registerDTO);
            customer.setAccount(account);
            customerRepository.save(customer);
        }
        if(role.getRoleName().equalsIgnoreCase("CARE_TAKER")){
            CareTaker careTaker = careTakerMapper.toCareTaker(registerDTO);
            careTaker.setAccount(account);
            careTakerRepository.save(careTaker);

            List<Long> selectedOptionDetailIds = registerDTO.getSelectedOptionDetailIds();
            List<OptionDetailsOfCareTaker> optionDetailsOfCareTakers = new ArrayList<>();

            for(Long optionDetailId : selectedOptionDetailIds){
                OptionsDetails optionsDetails = optionsDetailsRepository.findById(optionDetailId).orElseThrow(()->new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));
                OptionDetailsOfCareTaker optionDetailsOfCareTaker = new OptionDetailsOfCareTaker();
                optionDetailsOfCareTaker.setCare_taker(careTaker);
                optionDetailsOfCareTaker.setOptionsDetails(optionsDetails);
                optionDetailsOfCareTakers.add(optionDetailsOfCareTaker);
            }
            optionDetailsOfCareTakerRepository.saveAll(optionDetailsOfCareTakers);

            Image image = new Image();
            try {
                String imgProfilUrl = (imgProfile != null) ? cloudinaryService.uploadFile(imgProfile) : null;
                String imgCccdUrl = (imgCccd != null) ? cloudinaryService.uploadFile(imgCccd) : null;
                image.setImgProfile(imgProfilUrl);
                image.setImgCccd(imgCccdUrl);
                image.setCare_taker(careTaker);
                imageRepository.save(image);
            } catch (IOException e) {
                throw new ApiException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        return registerDTO;
    }
}

package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.DTO.Request.CareRecipientReq;
import com.example.Cap2.NannyNow.DTO.Response.AccountRes;
import com.example.Cap2.NannyNow.DTO.Response.CccdResponse;
import com.example.Cap2.NannyNow.DTO.Response.CccdWrapperResponse;
import com.example.Cap2.NannyNow.Entity.*;
import com.example.Cap2.NannyNow.Enum.EGender;
import com.example.Cap2.NannyNow.Enum.EStatusAccount;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.AccountMapper;
import com.example.Cap2.NannyNow.Mapper.CareRecipientMapper;
import com.example.Cap2.NannyNow.Mapper.CareTakerMapper;
import com.example.Cap2.NannyNow.Mapper.CustomerMapper;
import com.example.Cap2.NannyNow.Repository.*;
import com.example.Cap2.NannyNow.Service.Cloud.CloudinaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    EmailService emailService;
    AccountRepository accountRepository;
    Account_RoleRepository account_RoleRepository;
    RoleRepository roleRepository;
    CustomerRepository customerRepository;
    CareTakerRepository careTakerRepository;
    OptionDetailsOfCareTakerRepository optionDetailsOfCareTakerRepository;
    OptionsDetailsRepository optionsDetailsRepository;
    ImageRepository imageRepository;
    CareRecipientRepository careRecipientRepository;
    PasswordEncoder passwordEncoder;
    CloudinaryService cloudinaryService;
    AccountMapper accountMapper;
    CustomerMapper customerMapper;
    CareTakerMapper careTakerMapper;
    CareRecipientMapper careRecipientMapper;
    IDRecognitionService idRecognitionService;

    public Account getAccountByUserName(String userName) {
        return this.accountRepository.findByUserName(userName);
    }

    @Transactional
    public RegisterDTO register(RegisterDTO registerDTO, MultipartFile imgProfile, MultipartFile imgCccd) throws IOException {
        Role role = roleRepository.findByRoleName(registerDTO.getRoleName());
        if (role == null) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }
        if (accountRepository.existsByUsernameOrEmailOrPhoneNumber(registerDTO.getUserName(), registerDTO.getEmail(), registerDTO.getPhoneNumber())) {
            throw new ApiException(ErrorCode.MAIL_PHONE_USERNAME_ALREADY_EXITS);
        }
        Account account = accountMapper.toAccount(registerDTO);
        account.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        if (role.getRoleName().equalsIgnoreCase("CARE_TAKER")) {
            account.setActive(EStatusAccount.valueOf("PENDING"));
        }
        else{
            account.setActive(EStatusAccount.valueOf("ACTIVE"));
        }
        accountRepository.save(account);

        Account_Role account_role = new Account_Role();
        account_role.setAccount(account);
        account_role.setRole(role);
        account_RoleRepository.save(account_role);

        if (role.getRoleName().equalsIgnoreCase("CUSTOMER")) {
            Customer customer = customerMapper.toCustomer(registerDTO);
            customer.setAccount(account);
            customer = customerRepository.save(customer);

            if (registerDTO.getCareRecipient() != null) {
                CareRecipientReq careRecipientReq = registerDTO.getCareRecipient();
                CareRecipient careRecipient = careRecipientMapper.toCareRecipient(careRecipientReq);
                careRecipient.setCustomer(customer);
                careRecipientRepository.save(careRecipient);
            }

            if (registerDTO.getCareRecipients() != null && !registerDTO.getCareRecipients().isEmpty()) {
                for (CareRecipientReq careRecipientReq : registerDTO.getCareRecipients()) {
                    CareRecipient careRecipient = careRecipientMapper.toCareRecipient(careRecipientReq);
                    careRecipient.setCustomer(customer);
                    careRecipientRepository.save(careRecipient);
                }
            }
        }
        if (role.getRoleName().equalsIgnoreCase("CARE_TAKER")) {
            CareTaker careTaker = careTakerMapper.toCareTaker(registerDTO);
            careTaker.setAccount(account);
            careTakerRepository.save(careTaker);

            List<Long> selectedOptionDetailIds = registerDTO.getSelectedOptionDetailIds();
            List<OptionDetailsOfCareTaker> optionDetailsOfCareTakers = new ArrayList<>();

            for (Long optionDetailId : selectedOptionDetailIds) {
                OptionsDetails optionsDetails = optionsDetailsRepository.findById(optionDetailId).orElseThrow(() -> new ApiException(ErrorCode.OPTION_DETAIL_NOT_FOUND));
                OptionDetailsOfCareTaker optionDetailsOfCareTaker = new OptionDetailsOfCareTaker();
                optionDetailsOfCareTaker.setCare_taker(careTaker);
                optionDetailsOfCareTaker.setOptionsDetails(optionsDetails);
                optionDetailsOfCareTakers.add(optionDetailsOfCareTaker);
            }
            optionDetailsOfCareTakerRepository.saveAll(optionDetailsOfCareTakers);

            emailService.sendCaretakerRegistrationNotification(
                careTaker.getNameOfCareTaker(), 
                careTaker.getEmail(), 
                careTaker.getPhoneNumber()
            );
            
            Image image = new Image();
            try {
                String imgProfilUrl = (imgProfile != null) ? cloudinaryService.uploadFile(imgProfile) : null;
                String imgCccdUrl = null;
                if (imgCccd != null && !imgCccd.isEmpty()) {
                    imgCccdUrl = cloudinaryService.uploadFile(imgCccd);
                    // 2. Lưu file tạm để gửi FPT.AI
                    File tempFile = File.createTempFile("cccd-", ".jpg");
                    imgCccd.transferTo(tempFile); // chuyển MultipartFile -> File
                    // 3. Gửi đến FPT.AI để kiểm tra CCCD
                    String result = idRecognitionService.recognizeCCCD(tempFile);
                    System.out.println(result);
                    if (result == null || result.trim().isEmpty()) {
                        throw new ApiException(ErrorCode.INVALID_CCCD);
                    }
                    // 4. Xử lý nếu cần validate dữ liệu CCCD
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        CccdWrapperResponse response = mapper.readValue(result, CccdWrapperResponse.class);
                        if (response.getErrorCode() != 0 || response.getData().isEmpty()) {
                            throw new ApiException(ErrorCode.INVALID_CCCD);
                        }

                        CccdResponse data = response.getData().get(0);
                        EGender genderFromCccd = EGender.fromVietnamese(data.getSex());
                        if (!data.getName().equalsIgnoreCase(registerDTO.getNameOfUser())) {
                            throw new ApiException(ErrorCode.INVALID_CCCD);
                        }
//                        if (!compareDob(data.getDob(), registerDTO.getDob())) {
//                            throw new ApiException(ErrorCode.INVALID_DOB);
//                        }
//                        if (!genderFromCccd.equals(registerDTO.getGender())) {
//                            throw new ApiException(ErrorCode.INVALID_GENDER);
//                        }
                    } catch (JsonProcessingException e) {
                        throw new ApiException(ErrorCode.INVALID_CCCD);
                    }
                    tempFile.delete();
                }
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

//    public boolean compareDob(String dobString, Date dobObject) {
//        String[] patterns = {"dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy"};
//
//        for (String pattern : patterns) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//                sdf.setLenient(false);
//                Date parsedDob = sdf.parse(dobString);
//                // So sánh ngày sau khi parse
//                return parsedDob.equals(dobObject);
//            } catch (ParseException e) {
//                // ignore và thử pattern tiếp theo
//            }
//        }
//        // Nếu tất cả đều fail
//        return false;
//    }

    public List<AccountRes> getAllCustomerAccount() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountRes> results = new ArrayList<>();

        for (Account account : accounts) {
            List<Account_Role> role = account.getAccountRoles();

            if (role != null && !role.isEmpty()) {
                String roleName = role.get(0).getRole().getRoleName();

                if ("CUSTOMER".equalsIgnoreCase(roleName) && account.getCustomer() != null) {
                    Customer customer = account.getCustomer();
                    AccountRes user = new AccountRes(
                            account.getAccountId(),
                            customer.getCustomer_id(),
                            customer.getNameOfCustomer(),
                            customer.getEmail(),
                            customer.getAccount().getActive()
                    );
                    results.add(user);
                }
            }
        }
        return results;
    }

    public List<AccountRes> getAllCareAccount() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountRes> results = new ArrayList<>();

        for (Account account : accounts) {
            List<Account_Role> role = account.getAccountRoles();

            if (role != null && !role.isEmpty()) {
                String roleName = role.get(0).getRole().getRoleName();

                if ("CARE_TAKER".equalsIgnoreCase(roleName) && account.getCareTaker() != null) {
                    CareTaker careTaker = account.getCareTaker();
                    AccountRes user = new AccountRes(
                            account.getAccountId(),
                            careTaker.getCare_taker_id(),
                            careTaker.getNameOfCareTaker(),
                            careTaker.getEmail(),
                            careTaker.getImage().getImgProfile(),
                            careTaker.getAccount().getActive()
                    );
                    results.add(user);
                }
            }
        }
        return results;
    }

    public Account updateActive(Long accountId,String status){
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ApiException(ErrorCode.ACCOUNT_NOT_FOUND));
        if(account != null){
            account.setActive(EStatusAccount.valueOf(status));
            accountRepository.save(account);
        }
        return account;
    }

    public Map<String, Integer> getCareTakerCounts() {
        int totalCount = accountRepository.countCareTakers();
        int activeCount = accountRepository.countActiveCareTakers("ACTIVE");
        int inactiveCount = accountRepository.countActiveCareTakers("INACTIVE");
        
        Map<String, Integer> counts = new HashMap<>();
        counts.put("totalCount", totalCount);
        counts.put("activeCount", activeCount);
        counts.put("inactiveCount", inactiveCount);
        
        return counts;
    }
}

package com.example.Cap2.NannyNow.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class IDRecognitionService {

    private static final String API_URL = "https://api.fpt.ai/vision/idr/vnm";
    private static final String API_KEY = "0U46MdN01tRfumdLAHl4Y4YLKC9RXlj2";

    public String recognizeCCCD(File imageFile) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo body multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(imageFile));

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("api-key", API_KEY);

        // Tạo request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Gửi request
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        return response.getBody();
    }
}

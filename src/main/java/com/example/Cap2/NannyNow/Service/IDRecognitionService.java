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
    private static final String API_KEY = "9t5Z3Tv3RD2ovk5Qb4PO3xkXpwwuviPg";

    public String recognizeCCCD(File imageFile) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(imageFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("api-key", API_KEY);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Gửi request
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

        return response.getBody();
    }
}

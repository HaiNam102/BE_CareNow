package com.example.Cap2.NannyNow.Config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "vnpay")
public class VNPAYConfig {
    private String tmnCode;
    private String hashSecret;
    private String returnUrl;
    private String payUrl;
    private String apiVersion;
    private String command;
}

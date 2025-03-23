package com.example.Cap2.NannyNow.Enum;

public enum ELocationType {
    HOME("nhà"),
    HOSPITAL("bệnh viện");
    
    private final String value;
    
    ELocationType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
} 
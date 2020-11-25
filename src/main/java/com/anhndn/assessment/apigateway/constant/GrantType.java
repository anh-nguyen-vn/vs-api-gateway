package com.anhndn.assessment.apigateway.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GrantType {
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token");

    private String name;

    public static GrantType fromName(String name) {
        for (GrantType f : values()) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
}

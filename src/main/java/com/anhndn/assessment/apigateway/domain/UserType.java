package com.anhndn.assessment.apigateway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserType {
    private Integer id;
    private String name;

    public static final UserType USER = new UserType(1, "user");

    public static UserType fromId(Integer id) {
        if (id == null) {
            return null;
        }
        switch (id) {
            case 1:
                return USER;
            default:
                return null;
        }
    }
}
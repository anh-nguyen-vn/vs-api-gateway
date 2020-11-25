package com.anhndn.assessment.apigateway.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseStatus {
    private String code;
    private String message;

    public static final ResponseStatus SUCCESS = new ResponseStatus("success", "Success");
    public static final ResponseStatus GENERAL_ERROR = new ResponseStatus("010001", "Any error occur");
    public static final ResponseStatus INVALID_DATA_TYPE = new ResponseStatus("010002", "Field %s is in invalid data type");
}

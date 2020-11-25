package com.anhndn.assessment.apigateway.exception;

import com.anhndn.assessment.apigateway.domain.GeneralResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientHttpException extends RuntimeException {
    private GeneralResponse generalResponse;
}

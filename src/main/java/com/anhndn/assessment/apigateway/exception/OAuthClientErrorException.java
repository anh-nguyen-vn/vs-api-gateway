package com.anhndn.assessment.apigateway.exception;

import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OAuthClientErrorException extends ClientErrorException {
    private OauthErrorResponse oauthErrorResponse;
}

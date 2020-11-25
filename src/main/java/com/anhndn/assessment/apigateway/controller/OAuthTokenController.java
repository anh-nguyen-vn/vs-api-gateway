package com.anhndn.assessment.apigateway.controller;

import com.anhndn.assessment.apigateway.aspect.LogAround;
import com.anhndn.assessment.apigateway.constant.PayloadConstant;
import com.anhndn.assessment.apigateway.domain.GeneralResponse;
import com.anhndn.assessment.apigateway.factory.ResponseFactory;
import com.anhndn.assessment.apigateway.service.OAuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthTokenController {

    @Autowired
    private OAuthTokenService oAuthTokenService;

    @Autowired
    private ResponseFactory responseFactory;

    @LogAround(message = "revoke access token")
    @DeleteMapping(value = "/v${api.version}/oauth/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> revokeTokenByToken(@RequestHeader(PayloadConstant.ACCESS_TOKEN_KEY) String accessToken) {
        oAuthTokenService.revokeAccessToken(accessToken);

        ResponseEntity response = responseFactory.success();
        return response;
    }
}

package com.anhndn.assessment.apigateway.controller;

import com.anhndn.assessment.apigateway.aspect.LogAround;
import com.anhndn.assessment.apigateway.constant.GrantType;
import com.anhndn.assessment.apigateway.constant.HttpHeaderConstant;
import com.anhndn.assessment.apigateway.constant.LoggerConstant;
import com.anhndn.assessment.apigateway.controller.response.OauthAccessTokenResponse;
import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.anhndn.assessment.apigateway.domain.Payload;
import com.anhndn.assessment.apigateway.exception.InvalidCredentialException;
import com.anhndn.assessment.apigateway.factory.ResponseFactory;
import com.anhndn.assessment.apigateway.repository.entity.OAuthClientDetailsEntity;
import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;
import com.anhndn.assessment.apigateway.service.UserService;
import com.anhndn.assessment.apigateway.service.OAuthClientDetailsService;
import com.anhndn.assessment.apigateway.service.OAuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserOAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private OAuthTokenService oAuthTokenService;

    @Autowired
    private OAuthClientDetailsService oAuthClientDetailsService;

    @LogAround(message = "request user access token")
    @PostMapping(value = "/v${api.version}/user/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OauthAccessTokenResponse> requestProviderAccessToken(
            @RequestParam(name = "grant_type", required = false) String grantType,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "client_id", required = false) String clientId,
            @RequestParam(name = "scope", required = false) String scope,
            @RequestParam(name = "refresh_token", required = false) String refreshToken,
            @RequestHeader(name = HttpHeaderConstant.CLIENT_ID) String clientIdHeader) {
        log.info("Grant Type [{}] with client id [{}]", grantType, clientId);

        if (!clientIdHeader.equals(clientId)) {
            log.error("Client Ids in header and body are not consistent");
            return responseFactory.errorOauth(HttpStatus.BAD_REQUEST, OauthErrorResponse.INVALID_REQUEST);
        }
        if (GrantType.fromName(grantType) == null) {
            log.info("Invalid grant type");
            return responseFactory.errorOauth(HttpStatus.BAD_REQUEST, OauthErrorResponse.OAUTH_UNSUPPORTED_GRANT_TYPE);
        }
        ResponseEntity responseEntity = null;
        try {
            if (GrantType.PASSWORD.getName().equals(grantType)) {
                log.info("Authenticating provider user credential with [{}] username", username);
                responseEntity = this.generateAccessTokenForPassword(username, password, clientId, grantType, scope);
            } else if (GrantType.REFRESH_TOKEN.getName().equals(grantType)) {
                // TODO: implement grant type refresh token
            } else {
                log.info("Invalid grant type");
                return responseFactory.errorOauth(HttpStatus.BAD_REQUEST, OauthErrorResponse.OAUTH_UNSUPPORTED_GRANT_TYPE);
            }
        }  catch(Exception ex) {
            log.error("Got error while get access token", ex);
            responseEntity = responseFactory.errorOauth(HttpStatus.INTERNAL_SERVER_ERROR, OauthErrorResponse.SERVER_ERROR);
        }

        return responseEntity;
    }

    private ResponseEntity generateAccessTokenForPassword(String username, String password, String clientId, String grantType, String scope) {
        Payload payload;
        try {
            log.info("Authenticating credential");
            payload = userService.createPayload(username, password);
            log.info("Credential authenticated");
        } catch (InvalidCredentialException ex) {
            log.info("Authentication fail notification");
            return responseFactory.errorOauth(HttpStatus.BAD_REQUEST, OauthErrorResponse.ACCESS_DENIED);
        }

        OAuthClientDetailsEntity oAuthClientDetailsEntity = oAuthClientDetailsService.getByClientId(clientId);
        if (oAuthClientDetailsEntity == null) {
            log.error("oauth client id [{}] does not exist", clientId);
            return responseFactory.errorOauth(HttpStatus.BAD_REQUEST, OauthErrorResponse.OAUTH_CLIENT_NOT_FOUND);
        }

        OauthAccessTokenEntity oauthAccessTokenEntity = oAuthTokenService.createAccessToken(clientId, grantType, scope, payload);

        OauthAccessTokenResponse oauthAccessTokenResponse = new OauthAccessTokenResponse(oauthAccessTokenEntity, oAuthClientDetailsEntity);
        String correlationId = oauthAccessTokenResponse.getCorrelationId();
        MDC.put(LoggerConstant.CORRELATION_ID_LOG_KEY_NAME, correlationId);

        log.info("Login event success of [{}] userId", payload.getUserId());

        return new ResponseEntity<>(oauthAccessTokenResponse, HttpStatus.OK);
    }
}

package com.anhndn.assessment.apigateway.controller.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OauthErrorResponse {
    private String error;
    private String errorDescription;

    public static OauthErrorResponse OAUTH_CLIENT_NOT_FOUND = new OauthErrorResponse("client_not_found", "Client not found");
    public static OauthErrorResponse AUTHORIZATION_FIELD_MISSING = new OauthErrorResponse("authorization_is_missing", "Authorization is missing in the request header");
    public static OauthErrorResponse OAUTH_UNSUPPORTED_GRANT_TYPE = new OauthErrorResponse("unsupported_grant_type", "Authorization grant type is not supported");
    public static OauthErrorResponse ACCESS_TOKEN_EXPIRE = new OauthErrorResponse("access_token_expire", "Access token has been expired");
    public static OauthErrorResponse INVALID_ACCESS_TOKEN = new OauthErrorResponse("invalid_access_token", "Access token is invalid");
    public static OauthErrorResponse ACCESS_TOKEN_NOT_FOUND = new OauthErrorResponse("access_token_not_found", "Access token does not exist");
    public static OauthErrorResponse SERVER_ERROR = new OauthErrorResponse("server_error", "Server error");
    public static OauthErrorResponse ACCESS_DENIED = new OauthErrorResponse("access_denied", "Invalid credential");
    public static OauthErrorResponse INVALID_AUTHORIZATION = new OauthErrorResponse("invalid_authorization", "Invalid Authorization");
    public static OauthErrorResponse INVALID_REQUEST = new OauthErrorResponse("invalid_request", "Invalid Request");
    public static OauthErrorResponse INVALID_SCOPE = new OauthErrorResponse("access_denied", "Invalid Scope");
}

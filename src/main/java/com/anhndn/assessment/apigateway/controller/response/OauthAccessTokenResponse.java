package com.anhndn.assessment.apigateway.controller.response;

import com.anhndn.assessment.apigateway.constant.AccessTokenTypeConstant;
import com.anhndn.assessment.apigateway.domain.UserType;
import com.anhndn.assessment.apigateway.repository.entity.OAuthClientDetailsEntity;
import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;
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
public class OauthAccessTokenResponse {
    private String accessToken;
    private Integer accessTokenExpiresIn;
    private Integer expiresIn;
    private String tokenType;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
    private String correlationId;

    public OauthAccessTokenResponse(OauthAccessTokenEntity oauthAccessTokenEntity, OAuthClientDetailsEntity oAuthClientDetailsEntity) {
        this.accessToken = oauthAccessTokenEntity.getTokenValue();
        this.refreshToken = oauthAccessTokenEntity.getRefreshToken();
        this.tokenType = AccessTokenTypeConstant.BEARER;
        this.accessTokenExpiresIn = oAuthClientDetailsEntity.getAccessTokenValidity();
        this.correlationId = String.format("%s-%s", UserType.fromId(oauthAccessTokenEntity.getUserTypeId()).getName(), oauthAccessTokenEntity.getUserId());
        this.refreshTokenExpiresIn = oAuthClientDetailsEntity.getRefreshTokenValidity();
        this.expiresIn = oAuthClientDetailsEntity.getAccessTokenValidity();
    }
}

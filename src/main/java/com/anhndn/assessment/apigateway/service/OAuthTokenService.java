package com.anhndn.assessment.apigateway.service;

import com.anhndn.assessment.apigateway.domain.Payload;
import com.anhndn.assessment.apigateway.repository.OauthAccessTokenRepository;
import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OAuthTokenService {

    @Autowired
    private OauthAccessTokenRepository oauthAccessTokenRepository;

    public OauthAccessTokenEntity getOauthAccessToken(String tokenValue) {
        log.info("Retrieving access token");
        OauthAccessTokenEntity oauthAccessTokenEntity = oauthAccessTokenRepository.findByTokenValue(tokenValue);
        log.info("Retrieved access token");

        return oauthAccessTokenEntity;
    }

    public OauthAccessTokenEntity createAccessToken(String clientId, String grantType, String scope, Payload payload) {
        String tokenValue = UUID.randomUUID().toString();
        String refreshTokenValue = UUID.randomUUID().toString();

        OauthAccessTokenEntity oauthAccessTokenEntity = OauthAccessTokenEntity.builder()
                .tokenValue(tokenValue)
                .username(payload.getUsername())
                .clientId(clientId)
                .scope(scope)
                .userId(payload.getUserId())
                .userTypeId(payload.getUserType().getId())
                .refreshToken(refreshTokenValue)
                .authorizedGrantTypes(grantType)
                .build();


        log.info("Creating access token for user id [{}]", oauthAccessTokenEntity.getUserId());
        oauthAccessTokenRepository.save(oauthAccessTokenEntity);
        log.info("Access token was created for user id [{}]", oauthAccessTokenEntity.getUserId());

        return oauthAccessTokenEntity;
    }

    public void revokeAccessToken(String accessToken) {
        log.info("Revoking token");
        oauthAccessTokenRepository.deleteByTokenValue(accessToken);
        log.info("Token was revoked");
    }

    public boolean isAccessTokenExpired(OauthAccessTokenEntity oauthAccessTokenEntity) {
        return isTimeExpired(oauthAccessTokenEntity.getCreatedTimestamp().getTime(), oauthAccessTokenEntity.getOAuthClientDetailsEntity().getAccessTokenValidity());
    }

    private boolean isTimeExpired(long time, long validityTimeInSecond) {
        return time + validityTimeInSecond* 1000 < System.currentTimeMillis();
    }

}

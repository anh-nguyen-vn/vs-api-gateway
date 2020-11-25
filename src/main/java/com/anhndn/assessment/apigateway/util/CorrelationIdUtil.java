package com.anhndn.assessment.apigateway.util;

import com.anhndn.assessment.apigateway.domain.UserType;
import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;

public class CorrelationIdUtil {

    public static String createCorrelationId(OauthAccessTokenEntity oauthAccessTokenEntity){
        return String.format("%s-%s", UserType.fromId(oauthAccessTokenEntity.getUserTypeId()).getName(), oauthAccessTokenEntity.getUserId());
    }
}

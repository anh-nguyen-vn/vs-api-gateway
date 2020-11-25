package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.constant.AccessTokenTypeConstant;
import com.anhndn.assessment.apigateway.constant.GatewayConstant;
import com.anhndn.assessment.apigateway.constant.HttpHeaderConstant;
import com.anhndn.assessment.apigateway.constant.LoggerConstant;
import com.anhndn.assessment.apigateway.constant.PayloadConstant;
import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.anhndn.assessment.apigateway.exception.OAuthClientErrorException;
import com.anhndn.assessment.apigateway.repository.entity.ApiEntity;
import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;
import com.anhndn.assessment.apigateway.service.OAuthTokenService;
import com.anhndn.assessment.apigateway.util.CorrelationIdUtil;
import com.anhndn.assessment.apigateway.util.RegExUtil;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccessTokenPreFilter extends AbstractGatewayFilter {
    
    @Autowired
    private OAuthTokenService oAuthTokenService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        ApiEntity apiEntity = (ApiEntity) RequestContext.getCurrentContext().get(GatewayConstant.REQUESTED_API_ATTRIBUTE_FILTER);
        return apiEntity.getIsRequiredAccessToken() || RequestContext.getCurrentContext().getResponse().isCommitted();
    }

    @Override
    public Object run() {
        log.info("Start verify access token");
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            verifyAccessToken(ctx);
        } catch (OAuthClientErrorException e) {
            log.error("client error", e);
            sendServletResponse(HttpStatus.SC_UNAUTHORIZED , e.getOauthErrorResponse());
        }
        log.info("End createPayload access token");
        return null;
    }

    private void verifyAccessToken(RequestContext requestContext) {
        String authorizationValue  = requestContext.getRequest().getHeader(HttpHeaderConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(authorizationValue)) {
            throw new OAuthClientErrorException(OauthErrorResponse.AUTHORIZATION_FIELD_MISSING);
        }

        String accessToken = RegExUtil.getAccessToken(authorizationValue);
        String clientId = requestContext.getRequest().getHeader(HttpHeaderConstant.CLIENT_ID);
        if (authorizationValue.indexOf(AccessTokenTypeConstant.BEARER) != 0 || StringUtils.isEmpty(accessToken)) {
            throw new OAuthClientErrorException(OauthErrorResponse.INVALID_AUTHORIZATION);
        } else {
            OauthAccessTokenEntity oauthAccessTokenEntity = oAuthTokenService.getOauthAccessToken(accessToken);
            if (oauthAccessTokenEntity != null) {
                if(!oauthAccessTokenEntity.getClientId().equals(clientId)) {
                    log.error("Access token is invalid");
                    throw new OAuthClientErrorException(OauthErrorResponse.INVALID_ACCESS_TOKEN);
                }

                if (oAuthTokenService.isAccessTokenExpired(oauthAccessTokenEntity)) {
                    log.error("Access token is expired");
                    throw new OAuthClientErrorException(OauthErrorResponse.ACCESS_TOKEN_EXPIRE);
                }

                String correlationId = CorrelationIdUtil.createCorrelationId(oauthAccessTokenEntity);
                MDC.put(LoggerConstant.CORRELATION_ID_LOG_KEY_NAME, correlationId);
                requestContext.addZuulRequestHeader(PayloadConstant.USER_ID_KEY, String.valueOf(oauthAccessTokenEntity.getUserId()));
                requestContext.addZuulRequestHeader(PayloadConstant.USER_TYPE_ID_KEY, String.valueOf(oauthAccessTokenEntity.getUserTypeId()));
                requestContext.addZuulRequestHeader(PayloadConstant.USERNAME_KEY, oauthAccessTokenEntity.getUsername());
                requestContext.addZuulRequestHeader(PayloadConstant.ACCESS_TOKEN_KEY, accessToken);
                requestContext.addZuulRequestHeader(HttpHeaderConstant.CORRELATION_ID, correlationId);

            } else {
                log.error("Access token was not found");
                throw new OAuthClientErrorException(OauthErrorResponse.ACCESS_TOKEN_NOT_FOUND);
            }
        }
    }
}

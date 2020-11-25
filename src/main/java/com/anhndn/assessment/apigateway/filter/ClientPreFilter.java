package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.constant.GatewayConstant;
import com.anhndn.assessment.apigateway.constant.HttpHeaderConstant;
import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.anhndn.assessment.apigateway.repository.entity.ApiEntity;
import com.anhndn.assessment.apigateway.repository.entity.OAuthClientDetailsEntity;
import com.anhndn.assessment.apigateway.service.OAuthClientDetailsService;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class ClientPreFilter extends AbstractGatewayFilter {

    @Autowired
    private OAuthClientDetailsService oAuthClientDetailsService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("Start filter client");
        // TODO: implement validate client

        // find matched api
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String clientId = request.getHeader(HttpHeaderConstant.CLIENT_ID);
        String servletPath = request.getServletPath();
        String httpMethod = request.getMethod();

        ApiEntity apiEntity = this.findMatchedApi(clientId,httpMethod, servletPath);
        if (apiEntity == null) {
            log.error("no granted api match path [{}], http method [{}]", servletPath, httpMethod);
            sendServletResponse(HttpStatus.SC_UNAUTHORIZED, OauthErrorResponse.INVALID_SCOPE);
        }

        RequestContext.getCurrentContext().set(GatewayConstant.REQUESTED_API_ATTRIBUTE_FILTER, apiEntity);

        return null;
    }

    private ApiEntity findMatchedApi(String clientId, String httpMethod, String path) {
        log.info("finding api with http method [{}], path [{}] called by client id [{}]", httpMethod, path, clientId);
        path = path.trim();
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }

        OAuthClientDetailsEntity oAuthClientDetailsEntity = oAuthClientDetailsService.getByClientId(clientId);
        AntPathMatcher matcher = new AntPathMatcher();
        for (ApiEntity apiEntity : oAuthClientDetailsEntity.getClientScopes()) {
            if (matcher.match(apiEntity.getPattern(), path) && httpMethod.equalsIgnoreCase(apiEntity.getHttpMethod())) {
                log.info("match api id [{}] in client scope", apiEntity.getId());
                return apiEntity;
            }
        }

        return null;
    }
}

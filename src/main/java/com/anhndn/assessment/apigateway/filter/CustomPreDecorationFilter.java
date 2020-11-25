package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.constant.GatewayConstant;
import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.anhndn.assessment.apigateway.repository.entity.ApiEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import java.net.MalformedURLException;
import java.net.URL;

@Component
@Slf4j
public class CustomPreDecorationFilter extends AbstractGatewayFilter{

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        ApiEntity apiEntity = (ApiEntity) RequestContext.getCurrentContext().get(GatewayConstant.REQUESTED_API_ATTRIBUTE_FILTER);
        return apiEntity != null && context.sendZuulResponse();
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        String requestMethod = context.getRequest().getMethod();
        ApiEntity apiEntity = (ApiEntity) RequestContext.getCurrentContext().get(GatewayConstant.REQUESTED_API_ATTRIBUTE_FILTER);
        String requestURI = this.urlPathHelper.getServletPath(context.getRequest());
        String location = apiEntity.getService().getLocation();

        //strip prefix if need
        if(apiEntity.getPrefix() != null && requestURI.startsWith(apiEntity.getPrefix()))
            requestURI = requestURI.substring(apiEntity.getPrefix().length());

        context.put("requestURI", requestURI);
        if (location.startsWith("http:") || location.startsWith("https:")) {
            context.setRouteHost(getUrl(location));
            log.info("forward request to [{}{}] with [{}] method", location, requestURI, requestMethod);
        } else if (location.startsWith("forward:")) {
            String forwardUri = StringUtils.cleanPath(location.substring("forward:".length()) + requestURI);
            context.set("forward.to",forwardUri);
            context.setRouteHost(null);
            log.info("forward request to [{}] with [{}] method", forwardUri.replace("//", "/"), requestMethod);
        } else {
            log.info("Cannot forward to [{}] location", location);
            sendServletResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, OauthErrorResponse.SERVER_ERROR);
        }

        return null;
    }

    private URL getUrl(String target) {
        try {
            return new URL(target);
        }
        catch (MalformedURLException ex) {
            throw new IllegalStateException("Target URL is malformed", ex);
        }
    }
}

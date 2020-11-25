package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractGatewayFilter extends ZuulFilter {
    private static final String DEFAULT_CHARSET_NAME="UTF-8";
    private static final String DEFAULT_CONTENT_TYPE="application/json";

    @Autowired
    private ObjectMapper objectMapper;

    final protected void sendServletResponse(int statusCode, OauthErrorResponse oauthErrorResponse){

        try {
            String responseBody = objectMapper.writeValueAsString(oauthErrorResponse);
            RequestContext.getCurrentContext().setResponseStatusCode(statusCode);
            RequestContext.getCurrentContext().getResponse().setContentType(DEFAULT_CONTENT_TYPE);
            RequestContext.getCurrentContext().getResponse().setCharacterEncoding(DEFAULT_CHARSET_NAME);
            RequestContext.getCurrentContext().getResponse().getOutputStream().write(responseBody.getBytes(DEFAULT_CHARSET_NAME));
            RequestContext.getCurrentContext().getResponse().getOutputStream().flush();
            RequestContext.getCurrentContext().setSendZuulResponse(false);
        } catch (Exception e) {
            log.error("Generate response has got exception ",e);
        }
    }
}

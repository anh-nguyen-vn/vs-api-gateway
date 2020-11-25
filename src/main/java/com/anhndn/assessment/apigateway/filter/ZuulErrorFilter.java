package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.controller.response.OauthErrorResponse;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZuulErrorFilter extends AbstractGatewayFilter {

    private final Logger logger = LogManager.getLogger();

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getThrowable() != null;
    }

    @Override
    public Object run() {
        Throwable throwable = RequestContext.getCurrentContext().getThrowable();
        logger.error("Exception was thrown in filters: ", throwable);
        sendServletResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR , OauthErrorResponse.SERVER_ERROR);
        return null;
    }
}

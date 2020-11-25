package com.anhndn.assessment.apigateway.filter;

import com.anhndn.assessment.apigateway.constant.HttpHeaderConstant;
import com.anhndn.assessment.apigateway.constant.LoggerConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class LogFormatPreFilter extends ZuulFilter{

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        initLogFormat();
        return null;
    }

    private void initLogFormat() {
        String ipAddress = RequestContext.getCurrentContext().getRequest().getHeader(HttpHeaderConstant.X_IP_ADDRESS);
        MDC.put(LoggerConstant.IP_ADDRESS_LOG_KEY_NAME, ipAddress);
        MDC.put(LoggerConstant.CORRELATION_ID_LOG_KEY_NAME, LoggerConstant.DEFAULT_CORRELATION_ID);
        RequestContext.getCurrentContext().addZuulRequestHeader(HttpHeaderConstant.CORRELATION_ID, LoggerConstant.DEFAULT_CORRELATION_ID);
    }
}

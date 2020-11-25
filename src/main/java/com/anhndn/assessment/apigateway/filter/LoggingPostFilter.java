package com.anhndn.assessment.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingPostFilter extends ZuulFilter{

    @Override
    public String filterType() {
        return "post";
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
        String path = RequestContext.getCurrentContext().getRequest().getServletPath();
        log.info("End filter '{}' request", path);
        log.info("========== End filter request ==========");
        return null;
    }

}

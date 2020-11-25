package com.anhndn.assessment.apigateway.config;

import com.anhndn.assessment.apigateway.constant.HttpHeaderConstant;
import com.anhndn.assessment.apigateway.constant.LoggerConstant;
import com.anhndn.assessment.apigateway.domain.GeneralResponse;
import com.anhndn.assessment.apigateway.exception.ClientHttpException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@Slf4j
public class UserFeignConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor appendHeaders() {
        return template -> {
            template.header(HttpHeaderConstant.CORRELATION_ID, ThreadContext.get(LoggerConstant.CORRELATION_ID_LOG_KEY_NAME));
            template.header(HttpHeaderConstant.X_IP_ADDRESS, ThreadContext.get(LoggerConstant.IP_ADDRESS_LOG_KEY_NAME));
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String s, Response response) {
            try {
                GeneralResponse generalResponse = objectMapper.readValue(response.body().asInputStream(), GeneralResponse.class);
                ClientHttpException clientHttpException = new ClientHttpException(generalResponse);

                return clientHttpException;
            } catch (IOException e) {
                log.error("Cannot parse malformed http response from client", e);
                return e;
            }
        }
    }
}

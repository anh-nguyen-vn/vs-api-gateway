package com.anhndn.assessment.apigateway.client;

import com.anhndn.assessment.apigateway.client.request.CustomerPayloadCreateRequest;
import com.anhndn.assessment.apigateway.config.UserFeignConfig;
import com.anhndn.assessment.apigateway.domain.GeneralResponse;
import com.anhndn.assessment.apigateway.domain.Payload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", url = "${user-profile.base-path}", configuration = UserFeignConfig.class)
public interface UserClient {

    @PostMapping(value = "/internal/users/payloads", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    GeneralResponse<Payload> createPayload(@RequestBody CustomerPayloadCreateRequest request);

}

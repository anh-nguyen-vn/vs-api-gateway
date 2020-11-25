package com.anhndn.assessment.apigateway.service;

import com.anhndn.assessment.apigateway.client.UserClient;
import com.anhndn.assessment.apigateway.client.request.CustomerPayloadCreateRequest;
import com.anhndn.assessment.apigateway.domain.GeneralResponse;
import com.anhndn.assessment.apigateway.domain.Payload;
import com.anhndn.assessment.apigateway.exception.ClientHttpException;
import com.anhndn.assessment.apigateway.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserClient userClient;

    public Payload createPayload(String username, String password) {
        log.info("create customer payload with username [{}]", username);
        try {
            GeneralResponse<Payload> payloadResponse = userClient.createPayload(new CustomerPayloadCreateRequest(username, password));
            return payloadResponse.getData();
        } catch (ClientHttpException e) {
            log.error("create customer payload fail", e);
            throw new InvalidCredentialException("Customer credential is invalid");
        }
    }

}

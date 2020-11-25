package com.anhndn.assessment.apigateway.service;

import com.anhndn.assessment.apigateway.repository.OAuthClientDetailsRepository;
import com.anhndn.assessment.apigateway.repository.entity.OAuthClientDetailsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OAuthClientDetailsService {

    @Autowired
    private OAuthClientDetailsRepository oAuthClientDetailsRepository;

    public OAuthClientDetailsEntity getByClientId(String clientId) {
        log.info("finding client id [{}]", clientId);
        Optional<OAuthClientDetailsEntity> clientDetailsEntity = oAuthClientDetailsRepository.findById(clientId);
        if (!clientDetailsEntity.isPresent()) {
            log.error("client with id [{}] not found", clientId);
            return null;
        }
        log.info("found client id [{}] with is_deleted [{}]", clientId, clientDetailsEntity.get().getIsDeleted());

        return clientDetailsEntity.get().getIsDeleted() ? null : clientDetailsEntity.get();
    }


}

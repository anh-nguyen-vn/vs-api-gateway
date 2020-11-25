package com.anhndn.assessment.apigateway.repository;

import com.anhndn.assessment.apigateway.repository.entity.OauthAccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthAccessTokenRepository extends JpaRepository<OauthAccessTokenEntity, Long> {
    OauthAccessTokenEntity findByTokenValue(String tokenValue);
    Integer deleteByTokenValue(String tokenValue);
}

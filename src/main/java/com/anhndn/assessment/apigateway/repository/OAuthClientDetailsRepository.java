package com.anhndn.assessment.apigateway.repository;

import com.anhndn.assessment.apigateway.repository.entity.OAuthClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetailsEntity, String> {
}

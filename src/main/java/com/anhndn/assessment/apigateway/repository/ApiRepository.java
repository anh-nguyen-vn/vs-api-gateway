package com.anhndn.assessment.apigateway.repository;

import com.anhndn.assessment.apigateway.repository.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, Long> {
}

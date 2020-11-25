package com.anhndn.assessment.apigateway.repository;

import com.anhndn.assessment.apigateway.repository.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

}

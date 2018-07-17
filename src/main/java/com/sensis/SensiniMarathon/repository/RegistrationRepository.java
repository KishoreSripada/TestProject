package com.sensis.SensiniMarathon.repository;

import com.sensis.SensiniMarathon.model.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<RegisterEntity, Long> {
}
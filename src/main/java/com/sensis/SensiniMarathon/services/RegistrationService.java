package com.sensis.SensiniMarathon.services;

import com.sensis.SensiniMarathon.beans.RegistrationBean;
import com.sensis.SensiniMarathon.model.RegisterEntity;
import com.sensis.SensiniMarathon.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository repository;

    public void saveRegistrationDetails(RegistrationBean bean) {
        RegisterEntity entity = populateRegisterEntity(bean);
        repository.save(entity);
    }

    private RegisterEntity populateRegisterEntity(RegistrationBean bean) {
        RegisterEntity entity = new RegisterEntity();
        entity.setName(bean.getName());
        entity.setPassword(bean.getPassword());
        entity.setEmail(bean.getEmail());
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        return entity;
    }
}

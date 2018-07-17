package com.sensis.SensiniMarathon.services;

import com.sensis.SensiniMarathon.AbstractTest;
import com.sensis.SensiniMarathon.beans.RegistrationBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

@Transactional
public class RegistrationServiceTest extends AbstractTest {

    @Autowired
    private RegistrationService service;

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void saveRegistrationDetails() {
        service.saveRegistrationDetails(populateRegistrationBean("kishore", "test@gmail.com", "test1234"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveRegistrationDetailsForIncorrectValues() {
        service.saveRegistrationDetails(populateRegistrationBean("", "test@gmail.com", "test1234"));
    }

    private RegistrationBean populateRegistrationBean(
            String name, String email, String password ) {
        RegistrationBean bean = new RegistrationBean();
        bean.setName(name);
        bean.setEmail(email);
        bean.setPassword(password);
        return bean;
    }
}
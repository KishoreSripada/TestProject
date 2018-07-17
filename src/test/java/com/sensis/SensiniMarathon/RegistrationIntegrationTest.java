package com.sensis.SensiniMarathon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sensis.SensiniMarathon.beans.RegistrationBean;
import com.sensis.SensiniMarathon.controllers.RegistrationController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

public class RegistrationIntegrationTest extends AbstractTest {

    @Autowired
    private RegistrationController controller;

    @Before
    public void setUp() {
        super.setUp(controller);
    }

    @Test
    public void test_when_valid_input_then_return_success_response() throws Exception {
        String uri = "/sensis/marathon/register";
        String inputJson = prepareJson("kish1234","test@test.com","pass1234");

        MvcResult result = callRegisterWebService(uri, inputJson);
        int status = result.getResponse().getStatus();
        RegistrationBean bean = getRegistrationBean(result);

        Assert.assertEquals("Expect status : 200", 200, status);
        Assert.assertNotNull("Name cannot be null" , bean.getName());
        Assert.assertNotNull("Email cannot be null", bean.getEmail());
        Assert.assertNotNull("Password cannot be null", bean.getPassword());

    }

    private RegistrationBean getRegistrationBean(MvcResult result) throws java.io.IOException {
        String content = result.getResponse().getContentAsString();
        return mapFromJson(content, RegistrationBean.class);
    }

    private String prepareJson(String name, String email, String password) throws JsonProcessingException {
        RegistrationBean bean = new RegistrationBean();
        bean.setName(name);
        bean.setEmail(email);
        bean.setPassword(password);
        return mapToJson(bean);
    }
}
package com.sensis.SensiniMarathon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sensis.SensiniMarathon.AbstractTest;
import com.sensis.SensiniMarathon.beans.RegistrationBean;
import com.sensis.SensiniMarathon.services.RegistrationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

public class RegistrationControllerTest extends AbstractTest {

    @Mock
    private RegistrationService service;

    @InjectMocks
    private RegistrationController controller;

    @Before
    public void setUp() {
        super.setUp(controller);
        doNothing().when(service).saveRegistrationDetails(Mockito.any());
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

    @Test
    public void test_when_null_input_then_return_validation_error_response() throws Exception {
        String uri = "/sensis/marathon/register";
        String inputJson = prepareJson("","","pass1234");

        MvcResult result = callRegisterWebService(uri, inputJson);
        int status = result.getResponse().getStatus();

        RegistrationBean bean = getRegistrationBean(result);
        Assert.assertEquals( 200, status);
        Assert.assertEquals( 400, bean.getStatusCode());
        Assert.assertEquals("Bad Request : Name, Email and Password cannot be empty !", bean.getStatus());
        Assert.assertEquals( "", bean.getName());
        Assert.assertEquals( "", bean.getEmail());
        Assert.assertNotNull(bean.getPassword());
    }

    @Test
    public void test_when_null_input_then_return_email_error_response() throws Exception {
        String uri = "/sensis/marathon/register";
        String inputJson = prepareJson("kish1234","testafa.com","pass1234");

        MvcResult result = callRegisterWebService(uri, inputJson);
        int status = result.getResponse().getStatus();

        RegistrationBean bean = getRegistrationBean(result);
        Assert.assertEquals( 200, status);
        Assert.assertEquals( 400, bean.getStatusCode());
        Assert.assertEquals("Bad Request : Email format is incorrect. Please check !", bean.getStatus());
        Assert.assertNotNull(bean.getName());
        Assert.assertNotNull(bean.getEmail());
        Assert.assertNotNull(bean.getPassword());
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
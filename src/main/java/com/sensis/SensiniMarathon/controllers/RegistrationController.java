package com.sensis.SensiniMarathon.controllers;

import com.sensis.SensiniMarathon.beans.RegistrationBean;
import com.sensis.SensiniMarathon.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@RestController
public class RegistrationController extends BaseController {

    private final static Logger LOGGER = Logger.getLogger(RegistrationController.class.getName());

    private RegistrationService registrationService;

    @Autowired
    RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(method = RequestMethod.POST, value="/sensis/marathon/register")
    @ResponseBody
    public RegistrationBean registerRunner(@RequestBody RegistrationBean runnerDetailsBean) {
        LOGGER.log(Level.INFO, "Invoked registerRunner method");
        RegistrationBean registrationBean = validateInputDetails(runnerDetailsBean);
        if (HttpStatus.BAD_REQUEST.value() == registrationBean.getStatusCode())
            return registrationBean;
        registrationBean = populateSuccessfulRegistrationResponse(runnerDetailsBean);
        registrationService.saveRegistrationDetails(registrationBean);
        return registrationBean;
    }

    private RegistrationBean populateEmailValidationFailureResponse(RegistrationBean bean) {
        LOGGER.log(Level.INFO, "Email validation failed - preparing error response");
        bean.setStatus("Bad Request : Email format is incorrect. Please check !");
        bean.setStatusCode(400);
        return bean;
    }

    private RegistrationBean populateSuccessfulRegistrationResponse(@RequestBody RegistrationBean runnerDetailsBean) {
        LOGGER.log(Level.INFO, "Registration successful - preparing response");
        RegistrationBean registrationBean = new RegistrationBean();
        registrationBean.setName(runnerDetailsBean.getName());
        registrationBean.setEmail(runnerDetailsBean.getEmail());
        registrationBean.setPassword(runnerDetailsBean.getPassword());
        registrationBean.setStatus("User details captured successfully");
        registrationBean.setStatusCode(200);
        return registrationBean;
    }

    private RegistrationBean populateEmptyInputFieldsResponse(RegistrationBean bean) {
        LOGGER.log(Level.INFO, "Input fields are empty - Preparing error response");
        bean.setStatus("Bad Request : Name, Email and Password cannot be empty !");
        bean.setStatusCode(400);
        return bean;
    }

    private RegistrationBean validateInputDetails(RegistrationBean runnerDetailsBean) {
        LOGGER.log(Level.INFO, "Validate input fields for empty values and email format");
        if (!validateEmptyInputFields(runnerDetailsBean)) {
            return populateEmptyInputFieldsResponse(runnerDetailsBean);
        }
        if (!isEmailAddressValid(runnerDetailsBean.getEmail())) {
            return populateEmailValidationFailureResponse(runnerDetailsBean);
        }
        return runnerDetailsBean;
    }

    private boolean validateEmptyInputFields(RegistrationBean runnerDetailsBean) {
        LOGGER.log(Level.INFO, "Validate input fields for empty values");
        boolean isValidationSuccessful = true;
        if(StringUtils.isEmpty(runnerDetailsBean.getName()) ||
                StringUtils.isEmpty(runnerDetailsBean.getEmail()) ||
                StringUtils.isEmpty(runnerDetailsBean.getPassword())
                ) {
            isValidationSuccessful = false;
        }
        return isValidationSuccessful;
    }

    private boolean isEmailAddressValid(String email)
    {
        LOGGER.log(Level.INFO, "Validate input fields for valid email format");
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }
}
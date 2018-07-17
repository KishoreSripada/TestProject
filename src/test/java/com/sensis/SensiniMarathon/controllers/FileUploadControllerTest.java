
package com.sensis.SensiniMarathon.controllers;

import com.sensis.SensiniMarathon.AbstractTest;
import com.sensis.SensiniMarathon.services.FileUploadService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileUploadControllerTest extends AbstractTest {

    @Mock
    private FileUploadService service;

    @InjectMocks
    private FileUploadController controller;

    @Before
    public void setUp() {
        super.setUp(controller);
        when(service.uploadFile(Mockito.any())).thenReturn("ticket.JPG");
    }

    @Test
    public void should_upload_to_amazon_s3_when_file_is_valid() throws Exception {
        MockMultipartFile firstFile =
                new MockMultipartFile("file", "filename.txt", "multipart/form-data", "testfile".getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/sensis/marathon/uploadprofilepicture")
                .file(firstFile)).andExpect(status().is(200))
                .andExpect(content().string("File Upload Successful"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_file_is_null() throws Exception {
        mvc.perform(MockMvcRequestBuilders.multipart("/sensis/marathon/uploadprofilepicture")
                .file(null));
    }
}
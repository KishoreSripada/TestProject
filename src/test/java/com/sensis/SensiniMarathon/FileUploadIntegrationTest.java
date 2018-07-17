package com.sensis.SensiniMarathon;

import com.sensis.SensiniMarathon.controllers.FileUploadController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileUploadIntegrationTest extends AbstractTest {

    @Autowired
    private FileUploadController controller;

    @Before
    public void setUp() {
        super.setUp(controller);
    }

    @Test
    public void should_upload_to_amazon_s3_when_file_is_valid() throws Exception {
        MockMultipartFile firstFile =
                new MockMultipartFile("file", "filename.txt", "multipart/form-data", "testfile".getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/sensis/marathon/uploadprofilepicture")
                .file(firstFile)).andExpect(status().is(200))
                .andExpect(content().string("File Upload Successful"));
    }
}
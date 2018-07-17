package com.sensis.SensiniMarathon.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.sensis.SensiniMarathon.AbstractTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

public class FileUploadServiceTest extends AbstractTest {

    @InjectMocks
    private FileUploadService service;

    @Test
    public void uploadFile() {
        String fileUrl = service.uploadFile(fetchMultipartFile());
        Assert.assertThat(fileUrl, CoreMatchers.containsString("filename.txt"));
    }

    @Test(expected = AmazonServiceException.class)
    public void uploadFile_throw_AmazonServiceException() {
        FileUploadService serviceSpy = Mockito.spy(service);
        doThrow(new AmazonServiceException("test amazon service exception")).when(serviceSpy).uploadFileTos3bucket(Mockito.any(), Mockito.any());
        serviceSpy.uploadFile(fetchMultipartFile());
    }

    @Test(expected = SdkClientException.class)
    public void uploadFile_throw_SdkClientException() {
        FileUploadService serviceSpy = Mockito.spy(service);
        doThrow(new SdkClientException("test amazon service exception")).when(serviceSpy).uploadFileTos3bucket(Mockito.any(), Mockito.any());
        serviceSpy.uploadFile(fetchMultipartFile());
    }

    @Test(expected = Exception.class)
    public void uploadFile_throw_Exception() {
        FileUploadService serviceSpy = Mockito.spy(service);
        doThrow(new Exception("test general exception")).when(serviceSpy).uploadFileTos3bucket(Mockito.any(), Mockito.any());
        serviceSpy.uploadFile(fetchMultipartFile());
    }

    private MultipartFile fetchMultipartFile() {
        return  new MockMultipartFile("file", "filename.txt", "multipart/form-data", "testfile".getBytes());
    }
}
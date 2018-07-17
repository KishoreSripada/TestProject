package com.sensis.SensiniMarathon.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sensis.SensiniMarathon.controllers.FileUploadController;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileUploadService {

    private final static Logger LOGGER = Logger.getLogger(FileUploadController.class.getName());

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            LOGGER.log(Level.INFO, "filename : " + fileName);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        }
        catch(AmazonServiceException e) {
            LOGGER.log(Level.SEVERE, "Amazon S3 could not process the request due to the error : " + e);
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            LOGGER.log(Level.SEVERE, "Amazon S3 couldn't be contacted for a response : " + e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception while processing the upload file " + e);
            e.printStackTrace();
        }
        return fileUrl;
    }

    public void uploadFileTos3bucket(String fileName, File file) {
        LOGGER.log(Level.INFO, "In uploadFileTos3bucket : START");
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withRegion(endpointUrl)
                .withCredentials(new ProfileCredentialsProvider())
                .build();
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        LOGGER.log(Level.INFO, "In uploadFileTos3bucket : END");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
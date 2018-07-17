package com.sensis.SensiniMarathon.controllers;

import com.sensis.SensiniMarathon.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FileUploadController extends BaseController {

    private final static Logger LOGGER = Logger.getLogger(FileUploadController.class.getName());

    private FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @RequestMapping(value = "/sensis/marathon/uploadprofilepicture", method = RequestMethod.POST)
    public ResponseEntity submit(@RequestParam("file") MultipartFile file) {
        try {
            LOGGER.log(Level.INFO, "Received request to upload file");
            fileUploadService.uploadFile(file);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception while processing the upload file " + e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.log(Level.INFO, "File Upload Successful");
        return new ResponseEntity("File Upload Successful", HttpStatus.OK);
    }
}
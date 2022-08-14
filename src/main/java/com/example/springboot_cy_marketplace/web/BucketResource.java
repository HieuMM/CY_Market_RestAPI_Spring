package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bucket/")
public class BucketResource {

    private AmazonClient amazonClient;

    @Autowired
    BucketResource(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }
/*
* @author: HieuMM
* @since: 10-Jun-22 11:30 AM
* @description:  upload file lên s3 trong cy-team-1.1/question/  upload file in cy-team-1.1/question
* @update:
* */
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file,@RequestPart(value = "folder")String folderName) {
        return this.amazonClient.uploadFileTos3bucketIfNotExits(file, folderName);
    }

/*
* @author: HieuMM
* @since: 10-Jun-22 11:29 AM
* @description:  xóa file trong cy-team-1.1/question bằng tên file sau khi được upload /delete file in cy-team-1.1/question by name file after update
* @update:
* */
    @DeleteMapping("/deleteFile")
    public String deleteFileInFolder(@RequestPart(value = "url") String fileUrl ,@RequestPart(value = "folder")String folderName) {
        return this.amazonClient.deleteFileFromS3BucketInFolder(fileUrl,folderName);
    }
}
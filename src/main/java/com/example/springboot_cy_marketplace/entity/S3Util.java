package com.example.springboot_cy_marketplace.entity;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class S3Util {
    private String awsId;
    private String awsKey;
    private String bucketName;
    private String urlReturn;

    // fileName và pathFile bạn truyền từ một hàm  bên ngoài vào
    public Boolean upload(String fileName, String pathFile) throws IOException {
        getProperties();
        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsId, awsKey));
        try {
            File file = new File(pathFile);
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
            return true;
        } catch (AmazonServiceException e) {
            System.out.println("AmazonServiceException: "+ e.getMessage());
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException: "+ ace.getMessage());
        }
        return null;
    }

    public void getProperties() {
        Properties prop = new Properties();
        FileInputStream propFile1 = null;
        try {
            propFile1 =
                    new FileInputStream( "path.../amazon.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (propFile1 != null) {
                prop.load(propFile1);
                awsId = prop.getProperty("aws_access_key_id");
                awsKey = prop.getProperty("aws_secret_access_key");
                bucketName = prop.getProperty("aws_namecard_bucket");
                urlReturn = prop.getProperty("aws_url_return_prefix");
            }
        } catch (IOException ex) {
            System.out.println("getProperties: "+ ex);
        }
    }
}

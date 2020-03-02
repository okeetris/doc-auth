package com.raiseGreen.rg_auth_svc.cos;

import com.ibm.cloud.objectstorage.*;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.raiseGreen.rg_auth_svc.errorHandlers.DocNotFoundException;
import com.raiseGreen.rg_auth_svc.errorHandlers.UidNotFoundException;
import com.raiseGreen.rg_auth_svc.errorHandlers.UidNotPresentException;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Map;

import static com.raiseGreen.rg_auth_svc.ConfigSwitch.*;


public class CosExample
{

    public static String resultUrl;

    /**
     * @param args
     */
    public static void main(String[] args)
    {

        SDKGlobalConfiguration.IAM_ENDPOINT = iAMEndpoint;

        System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()).toString());
        AmazonS3 _cosClient = createClient();
    }

    /**
     * @return AmazonS3
     */
    public static AmazonS3 createClient()
    {
        AWSCredentials credentials;

        credentials = new BasicAWSCredentials(
                apiKey,
                secretKey
        );

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
    }



    public static String getPreUrl(String fileName, String uid){

        AmazonS3 cosClient = createClient();
        checkReqUid(uid, cosClient, fileName);


            try {

                // Set the presigned URL to expire.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                expTimeMillis += 1000 * 60 * Long.parseLong(expirationMilli);
                expiration.setTime(expTimeMillis);

                System.out.println("Generating pre-signed URL.");
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, fileName)
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = cosClient.generatePresignedUrl(generatePresignedUrlRequest);


                resultUrl = url.toString();


            } catch (Exception e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            }

            return resultUrl;

    }

    public static boolean checkReqUid(String uid, AmazonS3 _cosClient, String fileName){
        String reqUid;

        try {

            ObjectMetadata test = _cosClient.getObjectMetadata(bucketName, fileName);
            Map<String, String> test2 = test.getUserMetadata();
            reqUid = test2.get("uid");
        }
        catch (AmazonS3Exception e ){
            e.printStackTrace();
            throw new DocNotFoundException("Document" + fileName + " does not match any documents availible");
        }

        if (reqUid == null){
            throw new UidNotPresentException("This document does not have any meta-data");
        }

        if (reqUid.equals(uid)){
            return true;
        } else {
            throw new UidNotFoundException("uid "+uid+ " does not match the requested document" );
        }

    }

}
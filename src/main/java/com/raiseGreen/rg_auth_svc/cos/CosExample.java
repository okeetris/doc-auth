package com.raiseGreen.rg_auth_svc.cos;

import com.ibm.cloud.objectstorage.*;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.raiseGreen.rg_auth_svc.errorHandlers.UidNotFoundException;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Map;

public class CosExample
{

    private static AmazonS3 _cosClient;

    public static String bucketName = "rg-doc-store";  // eg my-unique-bucket-name

    public static String api_key = "afXmk5DPElbIQEDue8UwcUaJyFp4Vf1VpxD_FL28Dvvs"; // eg "W00YiRnLW4k3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
    public static String service_instance_id = "crn:v1:bluemix:public:cloud-object-storage:global:a/1a3800d393bc426c8202f7adb10ce7b0:222c580a-323a-41b3-a564-cef976bd2d0b:bucket:rg-doc-store"; // eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"
    public static String endpoint_url = "https://s3.eu-gb.cloud-object-storage.appdomain.cloud"; // this could be any service endpoint

    public static String storageClass = "eu-gb-standard";
    public static String location = "eu-gb";

    public static String resultUrl;

    /**
     * @param args
     */
    public static void main(String[] args)
    {

        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";


        String newBucketName = "test"; // eg my-other-unique-bucket-name


        AWSCredentials credentials = new BasicAWSCredentials(
                "41ccb967b38d4b97a1c26437ca2dc560",
                "0e2775f18559852d60fecdd315045e123ca61a9a1d95b512"
        );

        System.out.println("Current time: " + new Timestamp(System.currentTimeMillis()).toString());
        _cosClient = createClient();

        //listObjects(bucketName, _cosClient);
        //createBucket(newBucketName, _cosClient, storageClass);
        //listBuckets(_cosClient);
        //getPreUrl( "17_raise_green_inc_issuer_agreement-20200219143505-JJjKWZdK.pdf", Long.parseLong(args[0]));
        //getObject(_cosClient, bucketName, "17_raise_green_inc_issuer_agreement-20200219143505-JJjKWZdK.pdf");
    }

    /**
     * @return AmazonS3
     */
    public static AmazonS3 createClient()
    {
        AWSCredentials credentials;
        //test = new BasicIBMOAuthCredentials()
        //credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        credentials = new BasicAWSCredentials(
                "41ccb967b38d4b97a1c26437ca2dc560",
                "0e2775f18559852d60fecdd315045e123ca61a9a1d95b512"
        );

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return cosClient;
    }



    public static String getPreUrl(String fileName, String uid){

        AmazonS3 cosClient = createClient();
        boolean result = checkReqUid(uid, cosClient, fileName);

        if (result) {
            try {

                // Set the presigned URL to expire after one min.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                expTimeMillis += 1000 * 60;
                expiration.setTime(expTimeMillis);

                System.out.println("Generating pre-signed URL.");
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, fileName)
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = cosClient.generatePresignedUrl(generatePresignedUrlRequest);


                resultUrl = url.toString();
                // System.out.println("Pre-Signed URL: " + url.toString());


            } catch (SdkClientException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            }

            return resultUrl;
            //add test
        } else {
            //TODO: return error depending on comparison
            throw new UidNotFoundException("uid: "+uid+ " does not match the requested document" );
            //throw org.springframework.security.access.AccessDeniedException("403 returned");
        }
    }

    public static boolean checkReqUid(String uid, AmazonS3 _cosClient, String fileName){

        ObjectMetadata test = _cosClient.getObjectMetadata(bucketName,  fileName);
        Map<String, String> test2 = test.getUserMetadata();

        String reqUid = test2.get("uid");

        return reqUid.equals(uid);
        //TODO: Change key to uid value
        //TODO: compare uid to entered value
    }

/*
    public static void listObjects(String bucketName, AmazonS3 cosClient)
    {
        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    public static void listBuckets(AmazonS3 cosClient)
    {
        System.out.println("Listing buckets");
        final List<Bucket> bucketList = _cosClient.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
        System.out.println();
    }

    public static void getObject(AmazonS3 cosClient, String bucketName, String fileName){

        GetObjectRequest request = new // create a new request to get an object
                GetObjectRequest( // request the new object by identifying
                bucketName, // the name of the bucket
                fileName // the name of the object
        );
        //cosClient.generatePresignedUrl(bucketName, fileName, )
        cosClient.getObject( // write the contents of the object
                request, // using the request that was just created
                new File("retrieved.pdf") // to write to a new file
        );
    }*/

}
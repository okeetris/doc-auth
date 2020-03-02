package com.raiseGreen.rg_auth_svc;

public class ConfigSwitch {

    public static String expirationMilli;
    public static String bucketName;
    public static String endpoint_url;
    public static String location;
    public static String iAMEndpoint;
    public static String apiKey;
    public static String secretKey;

    public static void source(Boolean store) {

        if (store) {
            expirationMilli = System.getenv().get("EXPIRATION");
            bucketName = System.getenv().get("BUCKET_NAME");
            endpoint_url = System.getenv().get("ENDPOINT_URL");
            location = System.getenv().get("LOCATION");
            iAMEndpoint = System.getenv().get("IAM_ENDPOINT");
            apiKey = System.getenv().get("API_KEY");
            secretKey = System.getenv().get("SECRET_KEY");
        } else {
            expirationMilli = "1";
            bucketName = "rg-doc-store";
            endpoint_url = "https://s3.eu-gb.cloud-object-storage.appdomain.cloud";
            location = "eu-gb";
            iAMEndpoint = "https://iam.cloud.ibm.com/identity/token";
            apiKey = "41ccb967b38d4b97a1c26437ca2dc560";
            secretKey = "0e2775f18559852d60fecdd315045e123ca61a9a1d95b512";
        }
    }
}

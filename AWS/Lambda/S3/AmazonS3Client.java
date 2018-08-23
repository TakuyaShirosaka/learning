package com.aws.codestar.projecttemplates.handler;

import java.io.File;
import java.net.URL;
import java.util.Date;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;


public class AmazonS3Client {
    
	private static final String ENDPOINT_URL = "https://s3-ap-northeast-1.amazonaws.com";
    private static final String REGION       = "ap-northeast-1";
    private static final String ACCESS_KEY   = "アクセスキー";
    private static final String SECRET_KEY   = "シークレットキー";
    
	private AmazonS3 client;
	private Date expiration = new java.util.Date();
	private long expTimeMillis = expiration.getTime();
	
    public AmazonS3Client(Context context) {
        
    	LambdaLogger logger = context.getLogger();
        
    	// クライアント生成
        // 認証情報
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        // クライアント設定
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
        clientConfig.setConnectionTimeout(10000);   // 接続タイムアウト(ms) 

        // エンドポイント設定
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT_URL, REGION);

        // クライアント生成
        this.client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withClientConfiguration(clientConfig)
                        .withEndpointConfiguration(endpointConfiguration).build();
    }
    
    public void PutObject(String bucketName, String objectKey,File file) { 
        // アップロード
    	this.client.putObject(bucketName, objectKey,file);
    }
    
    public S3Object GetObject(String bucketName, String objectKey) { 
    	// ダウンロード    	
    	S3Object fullObject = this.client.getObject(new GetObjectRequest(bucketName, objectKey));

		return fullObject;
    }
    
    public String GeneratingURL(String bucketName, String objectKey) {
    	
        // 期限付きURL発行
        System.out.println("Generating pre-signed URL.");
        
        // 1時間だけ有効
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);    

        GeneratePresignedUrlRequest generatePresignedUrlRequest = 
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        
        URL url = this.client.generatePresignedUrl(generatePresignedUrlRequest);
        
        // TODO: implement your handler
        return url.toString();
    
    }
}


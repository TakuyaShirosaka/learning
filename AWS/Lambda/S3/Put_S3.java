package com.aws.codestar.projecttemplates.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class Put_S3 implements RequestHandler<Object, Object> {

    private static final String ENDPOINT_URL = "https://s3-ap-northeast-1.amazonaws.com";
    private static final String REGION       = "リージョンを指定する。";
    private static final String ACCESS_KEY   = "アクセスキーを指定する";
    private static final String SECRET_KEY   = "シークレットキーを指定する";
	
    @Override
    public Object handleRequest(final Object input, final Context context) {
    	//オブジェクトキーのイメージ：バケットの直下のファイルならファイル名＋拡張子、
    	//フォルダがある場合は"Test/Sample.png"みたいに指定してあげればよい
    	
    	String bucketName = "バケット名を指定する";
    	String objectKey = "オブジェクトキーを指定する";
    	
    	try {
			putObject(bucketName,objectKey);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    	return null;
    }
    
    //--------------------------------------------------
    // アップロード
    //--------------------------------------------------
    public void putObject(String bucketName, String objectKey) throws Exception {

        // クライアント生成
        AmazonS3 client = getClient(bucketName);
        
		//Lambdaは/tmp配下が作業領域として使えるので一時的にファイルを出力
        File file = new File("/tmp/sample.csv");
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		
    	//適当にファイルの中身を編集
        pw.println("2018/09/21");
        pw.close();
        
        // アップロード
        client.putObject(bucketName, objectKey,file);
    }

    //--------------------------------------------------
    // S3クライアント生成
	// 基本的にこの形で書けばダウンロードやデリートもできちゃいます。
    //--------------------------------------------------
    @SuppressWarnings("deprecation")
	private AmazonS3 getClient(String bucketName) throws Exception {

        // 認証情報
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        // クライアント設定
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
        clientConfig.setConnectionTimeout(10000);   // 接続タイムアウト(ms) 

        // エンドポイント設定
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT_URL, REGION);

        // クライアント生成
        AmazonS3 client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withClientConfiguration(clientConfig)
                        .withEndpointConfiguration(endpointConfiguration).build();

        if(!client.doesBucketExist(bucketName)) {
        // バケットがなければException
            throw new Exception("S3バケット[" + bucketName + "]がありません");
        }

        return client;
    }


}
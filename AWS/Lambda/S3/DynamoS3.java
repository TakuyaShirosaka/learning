package com.aws.codestar.projecttemplates.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class DynamoS3_ implements RequestHandler<Object, String> {
    
    @Override
    public String handleRequest(Object input, Context context) {
        
    	LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of DynamoS3");
        
        //DynamoDBクライアントを生成（別クラスでやっています。)
        AmazonDynamoDBClient DBClient = new AmazonDynamoDBClient(context);
        
        //QueryMemberSearch(自前で作ったGSIです。)を実行
        Iterator<Item> iterator = DBClient.QueryMemberSearch("UserName", 0, 99);
        
        //S3クライアントを生成（別クラスでやっています。)
        AmazonS3Client S3client = new AmazonS3Client(context);
        
    	String bucketName = "バケット名";
    	String objectKey = "オブジェクトキー";
        
        File file = new File("/tmp/ファイル名.csv");
        PrintWriter pw = null;
        
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		
			while (iterator.hasNext()) {
				//GSIの実行結果をCSVに書き込み
				String record = iterator.next().toJSON();            
				pw.println(record);
			}
                
			pw.close();
        
			// CSVファイルをS3にアップロード
			S3client.PutObject(bucketName, objectKey,file);
		
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
 
        // TODO: implement your handler
		//アップロードしたCSVファイルの有効期限付きURLをレスポンスとして返す
        return S3client.GeneratingURL(bucketName, objectKey).toString();
    
    }
}


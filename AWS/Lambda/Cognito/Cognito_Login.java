package com.aws.codestar.projecttemplates.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class Cognito_Login implements RequestStreamHandler {
    JSONParser parser = new JSONParser();
	
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		
        LambdaLogger logger = context.getLogger();        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //ステータスコード、ヘッダー、ボディ要素をまとめるもの
        JSONObject responseJson = new JSONObject();
        //ボディ要素を細かく設定するもの
		JSONObject responseBody = new JSONObject();
		//インプットを格納する
        JSONObject event = null;
        
		try {
			event = (JSONObject)parser.parse(reader);
			if (event.get("body") != null) {
				JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
           		Map<String, String> authParameters = new HashMap<>();
				authParameters.put("USERNAME", (String)body.get("USERNAME"));
				authParameters.put("PASSWORD", (String)body.get("PASSWORD"));
                
                //Lambdaの実行環境では資格情報の取得にEnvironmentVariableCredentialsProvider()を使用する。
                //リージョンの指定と資格情報をcredentialsProviderで付与
                AWSCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider();
                AWSCognitoIdentityProvider client = AWSCognitoIdentityProviderClientBuilder.standard()
                                                        .withCredentials(credentialsProvider)
                                                        .withRegion(Regions.AP_NORTHEAST_1)
                                                        .build();
                
                //Cognito側がサーバーベースの認証でサインイン API を有効にする（ADMIN_NO_SRP_AUTH）
                //PoolID,ClientIDの指定、これはAWSのコンソール上で確認する
                //認証情報をauthParametersで渡す。
                AdminInitiateAuthRequest request = new AdminInitiateAuthRequest();
                request.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                       .withUserPoolId("ユーザープールID")
                       .withClientId("クライアントID")
                       .withAuthParameters(authParameters);                

            	//ユーザーIDとパスワードを使ってIdToken,AccessTokenを取得
				//取得した結果を一旦オブジェクトにキャスト
				AdminInitiateAuthResult response = client.adminInitiateAuth(request);
				String IdToken = response.getAuthenticationResult().getIdToken();
				responseBody.put("IdToken", IdToken);
            }
			
		} catch (NotAuthorizedException|ParseException e) {
				e.printStackTrace();
		}finally {
			
			JSONObject headerJson = new JSONObject();
			//返却値の作成
			headerJson.put("Content-Type", "application/json");
			headerJson.put("Access-Control-Allow-Origin", "*");
			headerJson.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
			headerJson.put("Access-Control-Allow-Credentials", "true");
			
			responseJson.put("headers", headerJson);
			responseJson.put("statusCode", "200");
			
			//responseBodyからresponseJsonに格納する
			responseJson.put("body", responseBody.toString());  

			OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
			writer.write(responseJson.toJSONString());  
			writer.close();
		}
	}
}
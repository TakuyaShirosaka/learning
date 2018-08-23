package com.aws.codestar.projecttemplates.handler;

import java.util.Iterator;

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

public class AmazonDynamoDBClient  {
    
	private AmazonDynamoDB client;
	private DynamoDB dynamoDB;
	private Table table;
	private Index index;
	private QuerySpec spec;
	
    public AmazonDynamoDBClient(Context context) {
        
    	LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda AmazonDynamoDBClient");
        
        //グローバルセカンダリインデックスを使用した。クエリの実行
        this.client = AmazonDynamoDBClientBuilder.standard()
        .withRegion(Regions.AP_NORTHEAST_1).build();
        this.dynamoDB = new DynamoDB(client);
        
    }
    
    /*
     * GSI
     * XXXXXのインデックス
     */
    
    public Iterator<Item> QueryMemberSearch(String name, long st_age,long ed_age){
    	//Indexクラスをインスタンス化、クエリ名を指定してgetIndexを実行する。
        this.table = dynamoDB.getTable("テーブル名");
        this.index = table.getIndex("インデックス名");

        //.withProjectionExpression:指定した項目のみを選択する
        //.withKeyConditionExpression:SQLでいう条件文のようなもの、HashKey+SortKeyならばこんな感じの書き方になる
        //                            項目名 = :変数名  という書き方になる
        //.withValueMap:↑で定義した変数にMap形式で値をセットする。Stringなら.withStringで、数値なら.withNumberでセットする
        this.spec = new QuerySpec()
        		.withProjectionExpression("項目１,項目２")
                .withKeyConditionExpression("member_name = :v_member_name and member_age between :v_member_age_st and :v_member_age_ed")
                .withValueMap(new ValueMap().withString(":v_member_name", name).withNumber(":v_member_age_st", st_age).withNumber(":v_member_age_ed", ed_age));
        
        //クエリの実行
        ItemCollection<QueryOutcome> items = index.query(spec);
        Iterator<Item> iterator = items.iterator();
    	return iterator;
    }
}


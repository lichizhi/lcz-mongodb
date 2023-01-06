package com.lcz.study;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

public class MongoDBTest {

    private static final String DATABASE = "mashibing";

    public MongoCollection<Document> getDb(String collectionName) {
        ConnectionString connectionString = new ConnectionString("mongodb://192.168.129.141:27017/" + DATABASE);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).retryWrites(true).build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase(DATABASE).getCollection(collectionName);
    }

    @Test
    public void insert() {
        String collectionName = "orders2";
        MongoCollection<Document> db = getDb(collectionName);
        Document document = new Document();
        document.append("name", "手机");
        document.append("price", 8000);

        InsertOneResult result = db.insertOne(document);
        System.out.println(JSONObject.toJSONString(result, true));
    }

    @Test
    public void update() {
        String collectionName = "orders2";
        MongoCollection<Document> db = getDb(collectionName);
        Bson condition = Filters.and(Filters.eq("name", "手机"));
        Bson update = new Document("$set", new Document().append("price", 7000));

        db.updateMany(condition, update);
        System.out.println("更新成功");
    }

    @Test
    public void delete() {
        String collectionName = "orders2";
        MongoCollection<Document> db = getDb(collectionName);
        Bson condition = Filters.and(Filters.eq("name", "手机"));

        db.deleteMany(condition);
        System.out.println("删除成功");
    }

    @Test
    public void find() {
        String collectionName = "orders2";
        MongoCollection<Document> db = getDb(collectionName);
//        Bson condition = Filters.and(Filters.eq("name", "手机"));
//        FindIterable<Document> documents = db.find(condition);
        FindIterable<Document> documents = db.find();
        documents.forEach(document -> {
            System.out.println(JSONObject.toJSONString(document));
            System.out.println(document.get("name"));
        });
    }

}

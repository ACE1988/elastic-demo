//package com.example.elastic;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//
//import java.io.IOException;
//import java.util.Date;
//
//public class ElasticIndexApi {
//
//    public static void addCustomer() throws IOException {
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("10.100.11.118", 9200, "http")));
//
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "kimchy");
//            builder.field("postDate", new Date());
//            builder.field("message", "trying out Elasticsearch");
//        }
//        builder.endObject();
//        IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
//                .source(builder);
//
//        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//
//        client.close();
//
//    }
//
//    public static void getCustomer() throws  IOException{
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("10.100.11.118", 9200, "http")));
//
//        GetRequest getRequest = new GetRequest(
//                "posts",
//                "doc",
//                "1");
//        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//        System.out.println(getResponse.getSource());
//        System.out.println(getResponse.getId());
//        client.close();
//
//    }
//
//    public static void main(String [] args) throws  IOException{
////        addCustomer();
//        getCustomer();
//    }
//}

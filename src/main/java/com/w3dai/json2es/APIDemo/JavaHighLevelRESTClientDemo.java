package com.w3dai.json2es.APIDemo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class JavaHighLevelRESTClientDemo {
    private static RestHighLevelClient client;

    /*
    * create the connection to the elasticsearch.
    * */
    public static RestHighLevelClient createClient(){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        return client;
    }

    public void closeClient() throws IOException {
        client.close();
    }

    public static void main(String[] args) throws IOException{
        createClient();
        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);


        //the following is synchronous execution
        //IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);


        //the following is asynchronous execution
        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("failure");
            }
        };
        client.indexAsync(request, RequestOptions.DEFAULT, listener);


        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-get.html
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");

        //synchronous execution
        //GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        //Like the index API, the GET request also has a asynchronous fashion.

    }

}

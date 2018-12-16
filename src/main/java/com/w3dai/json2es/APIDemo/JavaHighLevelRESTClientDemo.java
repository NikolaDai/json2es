package com.w3dai.json2es.APIDemo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

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

    public static void main(String[] args) throws Exception{

        createClient();
        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "4");
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

        //other APIs like delete, update were omitted here. one thing to point out is that response from all the APIs could be checked

        //bulk API starts here
        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-bulk.html
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("posts", "doc", "1")
                .source(XContentType.JSON,"field", "foo"));
        bulkRequest.add(new IndexRequest("posts", "doc", "2")
                .source(XContentType.JSON,"field", "bar"));
        bulkRequest.add(new IndexRequest("posts", "doc", "3")
                .source(XContentType.JSON,"field", "baz"));

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        //attention: the document with the same id will be overwritten.
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();

            if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                IndexResponse indexResponse = (IndexResponse) itemResponse;

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                UpdateResponse updateResponse = (UpdateResponse) itemResponse;

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
            }
        }

        if (bulkResponse.hasFailures()) {
            System.out.println("At least one operation failed");
        }

        BulkProcessor.Listener testBulkListener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                System.out.println("Executing bulk"+  executionId +" with "+ numberOfActions +"requests");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (response.hasFailures()) {
                    System.out.println("Bulk"+ executionId + "executed with failures");
                } else {
                    System.out.println("Bulk "+ executionId + " completed in " + response.getTook().getMillis() + " milliseconds");
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println("Failed to execute bulk");
            }
        };

        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer =
                (requestTest, bulkListener) -> client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkListener);

        BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, testBulkListener);
        builder.setBulkActions(500);
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        builder.setConcurrentRequests(0);
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        builder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        BulkProcessor bulkProcessor = builder.build();

        IndexRequest one = new IndexRequest("posts", "doc", "1").
                source(XContentType.JSON, "title",
                        "In which order are my Elasticsearch queries executed?");
        IndexRequest two = new IndexRequest("posts", "doc", "2")
                .source(XContentType.JSON, "title",
                        "Current status and upcoming changes in Elasticsearch");
        IndexRequest three = new IndexRequest("posts", "doc", "3")
                .source(XContentType.JSON, "title",
                        "The Future of Federated Search in Elasticsearch");

        bulkProcessor.add(one);
        bulkProcessor.add(two);
        bulkProcessor.add(three);

        boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);

    }

}

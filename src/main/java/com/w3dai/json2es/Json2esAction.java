package com.w3dai.json2es;

import com.w3dai.json2es.APIDemo.JavaHighLevelRESTClientDemo;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class Json2esAction {
    public void readJasonAndWriteToES() throws IOException, InterruptedException {
        //Read the file of "dataSet01.txt" and write to es
        String filePath =  "./dataSet02.txt";

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        BulkRequest bulkRequest = new BulkRequest();

        BulkProcessor.Listener testBulkListener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                System.out.println("Executing bulk"+  executionId +" with "+ numberOfActions +" requests");
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
                System.out.println("Failed to execute bulk"+ executionId +"! " + failure.toString());

            }
        };

        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (requestTest, bulkListener) -> client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkListener);

        BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, testBulkListener);
        builder.setBulkActions(100000);
        builder.setBulkSize(new ByteSizeValue(50L, ByteSizeUnit.MB));
        builder.setConcurrentRequests(0);
        //builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        builder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        BulkProcessor bulkProcessor = builder.build();

        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt;

                int i = 0;
                while ((lineTxt = br.readLine()) != null) {
                    //lineTxt = lineTxt.replaceAll("\\\"", "\\\\\"");
                    int j = ++i;
                    //System.out.println(lineTxt);
                    //Plan to write to ES here
                    bulkRequest.add(new IndexRequest("articles", "article", String.valueOf(j)).source(lineTxt, XContentType.JSON));
                    bulkProcessor.add(new IndexRequest("articles", "article", String.valueOf(j)).source(lineTxt, XContentType.JSON));

                }

                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }

        boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);

        if(terminated){
            System.out.println("Successfully to import the json file to Elasticsearch!");
        }
        else{
            System.out.println("Failed to import the json file to Elasticsearch!");
        }

        client.close();
    }


}

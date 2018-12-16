package com.w3dai.json2es;

import com.w3dai.json2es.APIDemo.JavaHighLevelRESTClientDemo;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;

public class Json2esAction {
    public void readJasonAndWriteToES() throws  IOException{
        //Read the file of "dataSet01.txt" and write to es
        String filePath =  "./dataSet01.txt";

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt;

                BulkRequest bulkRequest = new BulkRequest();
                int i = 0;
                while ((lineTxt = br.readLine()) != null) {
                    lineTxt = lineTxt.replaceAll("\\\"", "\\\\\"");
                    System.out.println(lineTxt);
                    //Plan to write to ES here
                    bulkRequest.add(new IndexRequest("articles", "article", String.valueOf(++i)).source(lineTxt, XContentType.JSON));
                }

                BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

                if (bulkResponse.hasFailures()) {
                    System.out.println("At least one operation failed");
                }

                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }

        client.close();
    }


}

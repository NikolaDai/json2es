package com.w3dai.json2es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class CreateIndexFromData{
    public void CreateIndex() throws IOException {
        //create the high level client of the localhost
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        CreateIndexRequest request = new CreateIndexRequest("articles");

        request.settings(Settings.builder() // <1>
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );

        //please edit the following mapping on your needs.
        request.mapping("article", // <1>
                "{\n" +
                        "  \"article\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"eyebrowTitle\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-index\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"mainTitle\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-index\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"subTitle\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-index\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"authorsName\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-smart\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"editorsName\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-smart\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"pageName\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\"\n"+
                        "      },\n" +
                        "      \"columnName\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\"\n"+
                        "      },\n" +
                        "      \"paperCategory\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-smart\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      },\n" +
                        "      \"paperType\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\"\n"+
                        "      },\n" +
                        "      \"publishDate\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\":\"yyyyMMdd\"\n"+
                        "      },\n" +
                        "      \"articleText\": {\n" +
                        "        \"type\": \"text\",\n" +
                        "        \"index\":\"true\",\n"+
                        "        \"fielddata\":\"true\",\n"+
                        "        \"term_vector\": \"with_positions_offsets\",\n" +
                        "         \"analyzer\": \"hanlp-index\",\n" +
                        "         \"search_analyzer\": \"hanlp-smart\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);

        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isAcknowledged();

        System.out.println(acknowledged + "##" + shardsAcknowledged);

    }
}

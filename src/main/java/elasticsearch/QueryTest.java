package elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/13 21:52
 * @Description
 * @Version 1.0
 **/
public class QueryTest {

    private ElasticsearchClient client;

    @Before
    public void init() {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("aaaa", "bbbb"));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("cccc"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        // Create the low-level client
        RestClient restClient = builder.build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        this.client = client;
    }

    @After
    public void after() {

    }

    @Test
    public void helloWorld() throws IOException {
        List<FieldValue> catIds = new ArrayList<>();
        catIds.add(FieldValue.of(1));

        BoolQuery.Builder builder = QueryBuilders.bool();
        builder = builder.must(m -> m.match(mh -> mh.field("name").query(FieldValue.of("小米 11"))));
        if (catIds != null && catIds.size() > 0) {
            builder = builder.must(t -> t.terms(tq -> tq.field("cat_id").terms(TermsQueryField.of(v -> v.value(catIds)))));
        }

        BoolQuery.Builder finalBuilder = builder;
        SearchResponse<Product> search = client.search(s -> s
                        .index("dddd")
                        .query(q -> q.bool(finalBuilder.build()))
                        .from(0)
                        .size(5),
                Product.class);


        for (Hit<Product> hit: search.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    @Data
    static class Product {

        @JsonProperty("product_id")
        private Long productId;

        @JsonProperty("cat_id")
        private Long catId;

        @JsonProperty("brand_id")
        private Long brandId;

        private String name;

        @JsonProperty("image_url")
        private String imageUrl;

        @Override
        public String toString() {
            return "Product{" +
                    "productId=" + productId +
                    ", catId=" + catId +
                    ", brandId=" + brandId +
                    ", name='" + name + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }
    }
}

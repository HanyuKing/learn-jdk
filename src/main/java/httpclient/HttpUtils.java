package httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * http 工具类
 *
 */
public final class HttpUtils {
    public static volatile CloseableHttpClient HTTP_CLIENT = null;

    private static final String METHOD_POST = "POST";

    private static final String METHOD_PUT = "PUT";

    private static final String METHOD_GET = "GET";

    private static final Object SYNC_KEY = new Object();

    private static int defaultConnectionTimeout = 4 * 1000;

    private static int defaultSocketTimeout = 5 * 1000;

    private static int defaultConnectionRequestTimeout = 2 * 1000;

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    public static CloseableHttpClient getHttpClient() throws Exception {
        if (HTTP_CLIENT == null) {
            synchronized (SYNC_KEY) {
                if (HTTP_CLIENT == null) {
                    //使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书
                    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
                    // 该主机名验证程序实际上关闭了主机名验证。 它接受任何有效的SSL会话并匹配目标主机
                    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                    //为支持的协议方案创建自定义连接套接字工厂的注册表。
                    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https", sslsf)
                            .build();

                    //HTTPConnection工厂 ：配置请求/解析响应
                    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                            DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);

                    //DNS解析器
                    DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

                    //创建池化管理器
                    PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
                            socketFactoryRegistry, connFactory, dnsResolver);

                    poolingHttpClientConnectionManager = manager;

                    // 默认为Socket配置
                    SocketConfig socketConfig = SocketConfig.custom()
                            .setTcpNoDelay(true)
                            .build();
                    manager.setDefaultSocketConfig(socketConfig);
                    // 配置永久连接的最大总数或每个路由限制
                    // 可以保留在池中或由连接管理器租用。
                    //每个路由的默认最大连接，每个路由实际最大连接为默认为DefaultMaxPreRoute控制，而MaxTotal是控制整个池子最大数
                    manager.setMaxTotal(2048);
                    manager.setDefaultMaxPerRoute(1024);
                    // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
                    manager.setValidateAfterInactivity(5 * 1000);

                    //默认请求配置
                    //设置连接超时时间 4s
                    //设置等待数据超时时间，5s
                    //设置从连接池获取连接的等待超时时间
                    RequestConfig defaultRequestConfig = RequestConfig.custom()
                            .setConnectTimeout(defaultConnectionTimeout)
                            .setSocketTimeout(defaultSocketTimeout)
                            .setConnectionRequestTimeout(defaultConnectionRequestTimeout)
                            .build();

                    HTTP_CLIENT = HttpClients.custom()
                            .setConnectionManager(manager)
                            .setDefaultRequestConfig(defaultRequestConfig)
                            //连接池不是共享模式
                            .setConnectionManagerShared(false)
                            .evictIdleConnections(60, TimeUnit.SECONDS)
                            //定期回收空闲连接
                            .evictExpiredConnections()
                            .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                            //连接重用策略，即是否能keeplive
                            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                            //长连接配置，即获取长连接生产多长时间
                            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                            //设置重试次数，默认为3次;当前禁用掉(根据需要重启)
                            .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                            .build();

                    // jvm 停止或重启时，关闭连接池释放连接资源(跟数据库连接池类似)
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            HTTP_CLIENT.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }
        }
        return HTTP_CLIENT;
    }

    public static PoolingHttpClientConnectionManager getPoolCollectionManager() {
        return poolingHttpClientConnectionManager;
    }


    /**
     * 获取构建好的 POST、GET方法啥
     *
     * @param uri           uri
     * @param socketTimeout int
     * @return HttpPost 对象
     */
    private static HttpRequestBase newHttpRequest(String uri, String method, int socketTimeout) {
        // 构建 post、get 对象
        HttpRequestBase httpRequestBase = null;
        if (METHOD_POST.equals(method)) {
            httpRequestBase = new HttpPost(uri);
        } else if (METHOD_GET.equals(method)) {
            httpRequestBase = new HttpGet(uri);
        } else if (METHOD_PUT.equals(method)) {
            httpRequestBase = new HttpPut(uri);
        }

        /**
         * RequestConfig 默认配置
         * setSocketTimeout: 等待构建完 response 的最大时间
         * setConnectTimeout: 与服务器连接超时时间, http client 会创建一个异步线程用以创建 socket 连接, 此处设置该 socket 的连接超时时间
         * setConnectionRequestTimeout: 设置从 http client connect manager 连接池中获取 connection 链接超时时间
         */
        socketTimeout = socketTimeout <= 0 ? defaultSocketTimeout : socketTimeout;
        // 带超时时间 RequestConfig 配置
        httpRequestBase.setConfig(RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(defaultConnectionTimeout)
                .setConnectionRequestTimeout(defaultConnectionRequestTimeout)
                .build());
        return httpRequestBase;
    }

    /**
     * post http 请求
     *
     * @param uri     not null 请求地址
     * @param params  not null 请求参数体集
     * @param headers http header 集合
     * @return 应答体
     */
    public static final String post(String uri, Map<String, Object> params, Map<String, String> headers, int timeout) {
        headers = headers == null ? new HashMap<>() : headers;
        params = params == null ? new HashMap<>() : params;

        // 构建 http post 对象
        HttpPost httpPost = (HttpPost)newHttpRequest(uri, METHOD_POST, timeout);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        if (ContentType.APPLICATION_JSON.toString().equals(headers.get("Content-Type"))) {
            try {
                StringEntity requestEntity = new StringEntity(JSON.toJSONString(params),ContentType.APPLICATION_JSON.getCharset());
                httpPost.setEntity(requestEntity);
            } catch(UnsupportedCharsetException ex) {
                ex.printStackTrace();
            }
        } else {
            // set entity params
            List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
        }

        return exec(httpPost);
    }

    /**
     * get http 请求
     *
     * @param uri     请求地址
     * @param headers Map<String, String>
     * @return 应答体
     */
    public static final String get(String uri, Map<String, String> headers, int timeout) {
        headers = headers == null ? new HashMap<>() : headers;
        // 构建 http post 对象
        HttpGet httpGet = (HttpGet)newHttpRequest(uri,METHOD_GET, timeout);
        for (Map.Entry<String,String> entry : headers.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
        // 设置 http json header
        httpGet.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        return exec(httpGet);
    }

    /**
     * 执行 http 请求
     *
     * @param request HttpUriRequest
     * @return
     */
    protected static String exec(HttpRequestBase request) {
        CloseableHttpResponse response = null;
        try {
            // 用单例 HttpClient
            response = getHttpClient().execute(request);
            // 2xx is OK, 通过. 否则抛出异常
            int code = response.getStatusLine().getStatusCode();
            if (code >= HttpStatus.SC_OK && code < HttpStatus.SC_MULTIPLE_CHOICES) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (request != null) {
                    request.releaseConnection();
                }
            } catch (Throwable w) {
            }
        }
        return null;
    }
}
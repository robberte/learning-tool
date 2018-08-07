package com.robberte.learning.tool.http.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author robberte
 * @date 2018/7/31 上午12:39
 */
public class HttpSyncClient {

    private static int MAX_CONNECTION = 10;

    private static int DEFAULT_MAX_CONNECTION =  10;

    private String host;
    private int port;

    private CloseableHttpClient httpClient;

    public HttpSyncClient() {
        httpClient = createHttpSyncClient();
    }

    private CloseableHttpClient createHttpSyncClient() {
        return HttpClients.custom().setConnectionManager(getConnectionManager()).build();
    }

    public RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(2000) // 读数据超时时间
                .setConnectTimeout(3000) // 建立连接超时时间
                .setConnectionRequestTimeout(5000) // 请求连接池获取连接时间
                .build();
    }

    private PoolingHttpClientConnectionManager getConnectionManager() {
        HttpHost httpHost = new HttpHost(host, port);
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION);
        connectionManager.setMaxPerRoute(new HttpRoute(httpHost), 20);
        return connectionManager;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}

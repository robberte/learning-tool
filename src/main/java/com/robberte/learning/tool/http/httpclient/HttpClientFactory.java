package com.robberte.learning.tool.http.httpclient;

/**
 * @author robberte
 * @date 2018/7/31 上午12:33
 */
public class HttpClientFactory {

    private static HttpAsyncClient httpAsyncClient = new HttpAsyncClient();

    private static HttpSyncClient httpSyncClient = new HttpSyncClient();

    private static HttpClientFactory httpClientFactory = new HttpClientFactory();

    private HttpClientFactory() {
    }

    public static HttpClientFactory getInstance() {
        return httpClientFactory;
    }

    public HttpAsyncClient getHttpAsyncClient() {
        return httpAsyncClient;
    }

    public HttpSyncClient getHttpSyncClient() {
        return httpSyncClient;
    }

}

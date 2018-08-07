package com.robberte.learning.tool.http.httpclient;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;

/**
 * @author robberte
 * @date 2018/7/30 下午11:53
 */
public class HttpAsyncClient {

    private static int socketTimeout = 2000;
    private static int connectTimeout = 3000;
    private static int poolSize = 3000;
    private static int maxPerRouter = 1500;

    private String host;
    private int port;
    private String username;
    private String password;

    private CloseableHttpAsyncClient asyncClient;

    private CloseableHttpAsyncClient proxyAsyncClient;

    public HttpAsyncClient() {
        try {
            asyncClient = createAsyncClient(false);
            proxyAsyncClient = createAsyncClient(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CloseableHttpAsyncClient createAsyncClient(boolean proxy) throws IOReactorException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).build();


        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        SSLContext sslContext = SSLContexts.createDefault();
        Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslContext))
                .build();

        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();

        ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager conMgr = new PoolingNHttpClientConnectionManager(connectingIOReactor, null, registry, null);

        if (poolSize > 0) {
            conMgr.setMaxTotal(poolSize);
        }

        if (maxPerRouter > 0) {
            conMgr.setDefaultMaxPerRoute(maxPerRouter);
        } else {
            conMgr.setDefaultMaxPerRoute(10);
        }


        ConnectionConfig conectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();

        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                .build();

        conMgr.setDefaultConnectionConfig(conectionConfig);

        if(proxy) {
            return HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultConnectionConfig(conectionConfig)
                    .setProxy(new HttpHost(host, port)).build();
        } else {
            return HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultConnectionConfig(conectionConfig).build();
        }
    }

    public CloseableHttpAsyncClient getAsyncClient() {
        return asyncClient;
    }

    public CloseableHttpAsyncClient getProxyAsyncClient() {
        return proxyAsyncClient;
    }

}

package com.robberte.learning.tool.http.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author robberte
 * @date 2018/7/31 上午12:40
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static String DEFAULT_CHARSET_UTF8 = "UTF-8";

    public static String httpSyncPost(String url, List<BasicNameValuePair> list) {
        try {
            StringEntity stringEntity = new UrlEncodedFormEntity(list);
            return httpSyncPost(url, stringEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpSyncPost(String url, String body) {
        StringEntity stringEntity = null;
        if(StringUtils.isNotBlank(body)) {
            stringEntity = new StringEntity(body, DEFAULT_CHARSET_UTF8);
            stringEntity.setContentEncoding(DEFAULT_CHARSET_UTF8);
            stringEntity.setContentType("application/json");
        }
        return httpSyncPost(url, stringEntity);
    }

    private static String httpSyncPost(String url, StringEntity entity)  {
        CloseableHttpClient client = HttpClientFactory.getInstance().getHttpSyncClient().getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            if(entity != null) {
                logger.info("http sync post, params: {}", EntityUtils.toString(entity, DEFAULT_CHARSET_UTF8));
                httpPost.setEntity(entity);
            } else {
                logger.info("http sync post, params: null");
            }
            response = client.execute(httpPost);
            logger.info("http sync post, response code: {}", response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();
            String result = null;
            if(responseEntity != null) {
                result = EntityUtils.toString(responseEntity, DEFAULT_CHARSET_UTF8);
                logger.info("http sync post, response: {}", result);
            }
            EntityUtils.consume(responseEntity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}

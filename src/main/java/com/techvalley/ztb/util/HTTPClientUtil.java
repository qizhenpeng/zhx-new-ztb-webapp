package com.techvalley.ztb.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by doing on 14-1-3.
 */
public class HTTPClientUtil {
    private static CloseableHttpClient httpClient;

    /**
     * properties file example(notice.properties):
     * conn.request.timeout=5000
       conn.timeout=3000
       socket.timeout=5000
       max.conn.total=5
       max.conn.per.route=2
     *
     * @param
     */
    public synchronized static void init() {
        HttpClientConfig config = HttpClientConfigBuilder.create().build();
        if (httpClient == null) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(config.connReqTimeout)
                    .setConnectTimeout(config.connTimeout)
                    .setSocketTimeout(config.socketTimeout)
                    .build();
            httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(config.maxConnTotal)
                    .setMaxConnPerRoute(config.maxConnPerRoute)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
    }
    public synchronized static void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    public static String formPost(String url, Map<String, String> params, List<Header> headers) throws IOException {
        String result = null;
        boolean isNew = checkInit();
        try {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            if (headers != null && !headers.isEmpty()) {
                httpPost.setHeaders(headers.toArray(new Header[headers.size()]));
            }
            result = httpClient.execute(httpPost, new StringResponseHandler());
        } finally {
//            if (isNew) {
//                httpClient.close();
//            }
        }
        return result;
    }

    public static String stringEntityPost(String url, String params, List<Header> headers) throws IOException {
        String result = null;
        boolean isNew = checkInit();
        try {
            //StringEntity entity = new StringEntity(params, ContentType.APPLICATION_JSON);
            StringEntity entity = new StringEntity(params,"utf-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
                httpPost.setHeaders(headers.toArray(new Header[headers.size()]));
            }
            httpPost.setEntity(entity);
            System.out.println("start post..............");
            result = httpClient.execute(httpPost, new StringResponseHandler());
            System.out.println("end post...........");
        } finally {
//            if (isNew) {
//                System.out.println("close httpClient.....");
//                httpClient.close();
//            }
        }
        return result;
    }


    public static String httpGet(String url) throws IOException {
        String result = null;
        boolean isNew = checkInit();
        try {
            HttpGet httpGet = new HttpGet(url);
            result = httpClient.execute(httpGet, new StringResponseHandler());
        } finally {
//            if (isNew) {
//                httpClient.close();
//            }
        }
        return result;
    }

    public static void httpGetForFile(String url, String newFilePath) throws IOException {
        boolean isNew = checkInit();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();

            outputStream = new FileOutputStream(newFilePath);
            IOUtils.copy(inputStream, outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
//            if (isNew) {
//                httpClient.close();
//            }
        }
    }

    private static boolean checkInit() {
        boolean isNew = (httpClient == null);
        if (isNew) {
            httpClient = HttpClients.createDefault();
        }
        return isNew;
    }

    private static class StringResponseHandler implements ResponseHandler<String> {

        public String handleResponse(HttpResponse response) throws IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            } else {
                throw new ClientProtocolException("Unexpected response status: " + statusCode);
            }
        }
    }

    private static class HttpClientConfigBuilder {

        private HttpClientConfigBuilder() {
        }

        public static HttpClientConfigBuilder create() {
            return new HttpClientConfigBuilder();
        }

        HttpClientConfig build() {
//            Properties p = PropertiesUtils.loadProperties(ResourceLoader.CLASSPATH_URL_PREFIX + "/" + properties);
//            int connReqTimeout = Integer.valueOf(p.getProperty("conn.request.timeout", "5000"));
//            int connTimeout = Integer.valueOf(p.getProperty("conn.timeout", "3000"));
//            int socketTimeout = Integer.valueOf(p.getProperty("socket.timeout", "5000"));
//            int maxConnTotal = Integer.valueOf(p.getProperty("max.conn.total", "200"));
//            int maxConnPerRoute = Integer.valueOf(p.getProperty("max.conn.per.route", "10"));
            int connReqTimeout = 5000;
            int connTimeout = 3000;
            int socketTimeout =5000;
            int maxConnTotal = 20;
            int maxConnPerRoute = 10;
            return new HttpClientConfig(connReqTimeout, connTimeout, socketTimeout, maxConnTotal, maxConnPerRoute);
        }

    }

    private static class HttpClientConfig {
        int connReqTimeout;
        int connTimeout;
        int socketTimeout;
        int maxConnTotal;
        int maxConnPerRoute;

        private HttpClientConfig(int connReqTimeout, int connTimeout, int socketTimeout, int maxConnTotal, int maxConnPerRoute) {
            this.connReqTimeout = connReqTimeout;
            this.connTimeout = connTimeout;
            this.socketTimeout = socketTimeout;
            this.maxConnTotal = maxConnTotal;
            this.maxConnPerRoute = maxConnPerRoute;
        }
    }

}

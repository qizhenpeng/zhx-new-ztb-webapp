package com.techvalley.ztb.util;

/**
 * Created by Administrator on 2015/2/11.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.Random;

public class HttpClientUtils {
//    private static final Log log = LogFactory.getLog(HttpClientUtils.class);
    private static ThreadSafeClientConnManager cm = null;
    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));

        cm = new ThreadSafeClientConnManager(schemeRegistry);
        try {
            int maxTotal = 100;
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
//            log.error(
//                    "Key[httpclient.max_total] Not Found in systemConfig.properties",
//                    e);
        }
        // 每条通道的并发连接数设置（连接池）
        try {
            int defaultMaxConnection = 50;
            cm.setDefaultMaxPerRoute(defaultMaxConnection);
        } catch (NumberFormatException e) {
//            log.error(
//                    "Key[httpclient.default_max_connection] Not Found in systemConfig.properties",
//                    e);
        }
    }

    public static HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 3000ms
        return new DefaultHttpClient(cm, params);
    }

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }

    public static void main(String[] args) throws ClientProtocolException,
            IOException {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            long l1 = System.currentTimeMillis();
            HttpClient client = getHttpClient();

            HttpGet get = new HttpGet("http://192.168.1.100/parse-server/tika/get/"
                    + "9c06e0cb-1490-433d-b48b-f23f949a7fab");
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                long l = entity.getContentLength();
                System.out.println("回应结果长度:" + l);
            }
            System.out.println("查询耗时" + (System.currentTimeMillis() - l1));
        }
    }

}

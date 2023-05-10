package cn.cdsoft.modules.qxz.wx.util;

/**
 * Created by ZZUSER on 2018/10/26.
 */

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientCustomContext {
    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {              CookieStore cookieStore = new BasicCookieStore();
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);
            HttpGet httpget = new HttpGet(
                    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd2529c6c2aa9234f&secret=14c680cb58f4b835589a21d1b8ff24e1");
            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget,                      localContext);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                InputStream is = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String str = "";
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }              }
                finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}


package com.iceberg.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
  public static Integer doGet(String url) {
    // get请求返回结果
    Integer status = 0;
    try {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        status = response.getStatusLine().getStatusCode();
      }
    } catch (IOException e) {
      System.out.println("get request failed:" + url);
    }
    return status;
  }

  public static String doPost(String url, Map<String, String> paramMap) {
    // create HttpClient object
    CloseableHttpClient httpClient = HttpClients.createDefault();
    // create request
    HttpPost httpPost = new HttpPost(url);
    CloseableHttpResponse response = null;
    try {
      if (paramMap != null) {
        List<BasicNameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
          list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity httpEntity = new UrlEncodedFormEntity(list, "utf-8");
        httpPost.setEntity(httpEntity);
      }
      response = httpClient.execute(httpPost);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        String result1 = EntityUtils.toString(entity, "utf-8");
        EntityUtils.consume(entity);
        httpClient.close();
        return result1;
      }
      httpClient.close();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

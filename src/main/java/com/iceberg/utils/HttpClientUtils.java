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
  private static String tokenString = "";
  private static String AUTH_TOKEN_EXPIRED = "AUTH_TOKEN_EXPIRED";
  private static CloseableHttpClient httpClient = null;

  public static String doGet(String url) {
    // get请求返回结果
    String strResult = "";
    try {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        strResult = EntityUtils.toString(response.getEntity());
      }
    } catch (IOException e) {
      System.out.println("get请求提交失败:" + url);
    }
    return strResult;
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

  public static String doGet2(String url, String token) {
    // 创建HttpClient对象
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);

    try {
      // api_gateway_auth_token自定义header头，用于token验证使用
      get.addHeader("Bearer Token", token);
      HttpResponse response = httpClient.execute(get);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 返回json格式
        String res = EntityUtils.toString(response.getEntity());
        return res;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void doGetWithBearerToken() throws IOException {
    URL url = new URL("https://api.github.com/user");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestProperty("Authorization", "Bearer " + "4734a4f7f0b6ac362ef99234ebcd56b87759773e");
    // e.g. bearer token=
    // eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String output;

    StringBuffer response = new StringBuffer();
    while ((output = in.readLine()) != null) {
      response.append(output);
    }

    in.close();
    // printing result from response
    System.out.println("Response:-" + response.toString());
  }
}

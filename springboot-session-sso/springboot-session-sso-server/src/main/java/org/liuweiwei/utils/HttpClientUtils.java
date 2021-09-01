package org.liuweiwei.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Liuweiwei
 * @since 2021-08-27
 */
@Component
public class HttpClientUtils {

    private static RestTemplate restTemplate;

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        HttpClientUtils.restTemplate = restTemplate;
    }

    public static <T> T doPost(String url, Map<String, ? extends Object> params, Class<T> resultType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, ? extends Object>> httpEntity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, resultType);
        return responseEntity.getBody();
    }

    public static <T> T doGet(String url, Class<T> resultType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, resultType);
        return responseEntity.getBody();
    }

    public static <T> T doFrom(String url, LinkedMultiValueMap<String, ? extends Object> params, Class<T> resultType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, ? extends Object>> httpEntity = new HttpEntity(params, httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, resultType);
        return responseEntity.getBody();
    }

    // ------ 华丽的分割线 ------

    public static String doGet(String url, Map<String, String> param) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        for (String key : param.keySet()) {
            uriBuilder.addParameter(key, param.get(key));
        }
        HttpGet get = new HttpGet(uriBuilder.build());
        CloseableHttpResponse httpResponse = httpClient.execute(get);
        String resultMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        httpResponse.close();
        httpClient.close();
        return resultMsg;
    }

    public static String doPost(String url, Map<String, String> param) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (String key : param.keySet()) {
            paramList.add(new BasicNameValuePair(key, param.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramList));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
        response.close();
        httpClient.close();
        return resultMsg;
    }
}

package org.liuweiwei.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-08-28
 */
@Log4j2
public class HttpClientUtil {

    /**
     * 使用Json格式发送Post请求
     *
     * @param url         发送的URL
     * @param requestData 传给数据
     * @return
     * @throws IOException
     */
    public ResponseEntity postAction(String url, Object requestData) throws IOException {
        //将Json对象转换为字符串
        String jsonStr = JSONObject.toJSONString(requestData);
        log.info("httpClient发送数据：{}", jsonStr);

        //使用帮助类HttpClients创建CloseableHttpClient对象.
        CloseableHttpClient client = HttpClients.createDefault();

        //HTTP请求类型创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        //组织数据
        StringEntity se = new StringEntity(jsonStr);
        se.setContentType("application/json");

        //对于httpPost请求,把请求体填充进HttpPost实体.
        httpPost.setEntity(se);

        //执行请求
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            jsonStr = EntityUtils.toString(entity, "UTF-8").trim();
            Object cast = VerifyUtil.cast(jsonStr);
            // 处理子系统局部会话的Header认证
            if (null != response.getFirstHeader("x-auth-token")) {
                String string = response.getFirstHeader("x-auth-token").toString().split(" ")[1];
            }
        }
        response.close();
        client.close();
        return null;
    }
}

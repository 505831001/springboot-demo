package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.utils.HttpUtils;
import org.liuweiwei.utils.ResultData;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@RestController
@RequestMapping(value = "/authorize")
@Api(value = "", tags = "SSO客户端页面控制器")
@Log4j2
public class AuthorizeController {

    @Resource
    private AuthorizationCodeResourceDetails baseOAuth2ProtectedResourceDetails;
    @Resource
    private ResourceServerProperties resourceServerProperties;
    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/login")
    @ApiOperation(value = "SingleSignOn单点登录接口", notes = "SingleSignOn单点登录接口", tags = "")
    public ResultData login(String code, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(code)) {
            return ResultData.failure(code);
        } else {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            valueMap.add("client_id", baseOAuth2ProtectedResourceDetails.getClientId());
            valueMap.add("client_secret", baseOAuth2ProtectedResourceDetails.getClientSecret());
            valueMap.add("grant_type", baseOAuth2ProtectedResourceDetails.getGrantType());
            valueMap.add("redirect_uri", baseOAuth2ProtectedResourceDetails.getPreEstablishedRedirectUri());
            valueMap.add("code", code);
            Map<String, String> map = HttpUtils.doFrom(baseOAuth2ProtectedResourceDetails.getAccessTokenUri(), valueMap, Map.class);
            System.out.println(map);
            //获取用户信息，说明这里主要目的就是通过资源服务器去获取用户信息
            Map principal = HttpUtils.doGet(resourceServerProperties.getUserInfoUri() + "?access_token=" + map.get("access_token"), Map.class);

            //这里通过本地登录单点登录
            String username = principal.get("name").toString();
            //如果用户存在则不添加，这里如果生产应用中，可以更具规则修改
            if (StringUtils.isEmpty(username)) {
                username = "admin";
            }
            //这里通过本地登录的方式来获取会话
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("username", username);
            params.add("password", username);
            HttpEntity<LinkedMultiValueMap<String, ? extends Object>> httpEntity = new HttpEntity(params, httpHeaders);
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/login";
            ResponseEntity<Object> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
            //将登录后的header原本的给浏览器，这就是当前浏览器的会话
            HttpHeaders headers = exchange.getHeaders();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                entry.getValue().stream().forEach(value -> response.addHeader(entry.getKey(), value));
            }
            //这个状态是根据security的返回数据设定的
            response.setStatus(exchange.getStatusCode().value());
            log.info("请求Url.login()");
            return ResultData.success(HttpStatus.OK);
        }
    }
}

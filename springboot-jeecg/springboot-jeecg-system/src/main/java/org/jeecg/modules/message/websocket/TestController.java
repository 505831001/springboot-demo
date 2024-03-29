package org.jeecg.modules.message.websocket;

import org.jeecg.common.vo.Result;
import org.jeecg.common.constant.WebsocketConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("webSocketApi")
public class TestController {
	
    @Autowired
    private WebSocket webSocket;
 
    @PostMapping("/sendAll")
    public Result<String> sendAll(@RequestBody JSONObject jsonObject) {
    	Result<String> result = new Result<String>();
    	String message = jsonObject.getString("message");
    	JSONObject obj = new JSONObject();
    	obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_TOPIC);
		obj.put(WebsocketConst.MSG_ID, "M0001");
		obj.put(WebsocketConst.MSG_TXT, message);
    	webSocket.sendAllMessage(obj.toJSONString());
        result.setResult("群发！");
        return result;
    }

    @PostMapping("/sendUser")
    public Result<String> sendUser(@RequestBody JSONObject jsonObject) {
    	Result<String> result = new Result<String>();
    	String userId = jsonObject.getString("userId");
    	String message = jsonObject.getString("message");
    	JSONObject obj = new JSONObject();
    	obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
    	obj.put(WebsocketConst.MSG_USER_ID, userId);
		obj.put(WebsocketConst.MSG_ID, "M0001");
		obj.put(WebsocketConst.MSG_TXT, message);
        webSocket.sendOneMessage(userId, obj.toJSONString());
        result.setResult("单发");
        return result;
    }
    
}
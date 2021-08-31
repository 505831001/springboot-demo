<html>
<head>测试SSO统一登录中心</head>
<body>
<div>
    <hr/>
    请求头URL地址：${clientUrl}
    <br/>
    此次会话ID数值：${sessionId}
    <br/>
    <form id="login" action="/login" method="post">
        用户名称：<input type="text" name="username" , value="guest"/>
        <br/>
        用户密码：<input type="password" name="password" , value="12345"/>
        <input type="hidden" name="clientUrl" , value="${(clientUrl)?default("")}"/>
        <br/>
        <input type="submit" value="Login"/>
    </form>
</div>
</body>
</html>
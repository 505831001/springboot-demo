<html>

<head>
    <title>价值源自分享！</title>
    <script type="text/javascript" src="${base}/static/plugins/vue/2.6.9/vue.min.js"></script>
    <!--网络请求框架-->
    <script src="${base}/static/plugins/axios/0.18.0/axios.min.js"></script>
    <script src="${base}/static/plugins/qs/6.6.0/qs.min.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.http.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.util.js"></script>
</head>
<body style="padding-top: 5%;background-color: #ffffff;">
<center> <img src="https://cdn.mingsoft.net/global/mstore/true.png" />
    <div id="app">
        <#if result.data?has_content>
        <p style="clear: both; margin: 30px auto 20px auto; line-height: 35px; font-size: 20px; color: #7e7e7e;">安装成功，请下载源码，请按照插件说明操作并使用！<br/>
        <form method="post" target='_blank' action="http://store.mingsoft.net/store/client/plugin/download.do">
            <input name="id" style="display:none" value="${result.data.id}">
            <input name="token" style="display:none" value="${result.data.token}">
            <input type="submit"  value="下载源码" style='    width: 104px;height: 34px;border: none;background-color: #0099ff;color: #ffffff;font-size: 14px;cursor: pointer;' />
        </form>
        <#else>
        <p style="clear: both; margin: 30px auto 20px auto; line-height: 35px; font-size: 20px; color: #7e7e7e;">安装成功，请按照插件说明操作并使用！！
        </#if>
        </p>
    </div>
</center>
</body>
</html>


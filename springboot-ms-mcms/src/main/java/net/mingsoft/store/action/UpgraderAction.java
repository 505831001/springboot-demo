//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mingsoft.store.action;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.action.BaseAction;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.biz.IRoleModelBiz;
import net.mingsoft.basic.entity.ModelEntity;
import net.mingsoft.basic.entity.RoleModelEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("upgrader")
@RequestMapping({"/${ms.manager.path}/store"})
public class UpgraderAction extends BaseAction {
    private String html = "<html><head><title>价值源自分享！</title></head><body style=\"padding-top: 5%;background-color: #ffffff;\"><center>\t<img src=\"https://cdn.mingsoft.net/global/mstore/{result/}.png\" />\t<div>\t\t<p style=\"clear: both; margin: 30px auto 20px auto; line-height: 35px; font-size: 20px; color: #7e7e7e;\">{message/}</p>\t</div></center></body></html>";
    @Value("${ms.mstore-url:http://store.mingsoft.net}")
    private String MS_MSTORE_HTTP;
    @Value("${ms.mstore-host:store.mingsoft.net}")
    private String MS_MSTORE_HOST;
    @Autowired
    private IModelBiz modelBiz;
    @Autowired
    private IRoleModelBiz roleModelBiz;
    @Autowired
    private DataSource dataSource;

    public UpgraderAction() {
    }

    @RequestMapping(
            value = {"/sync"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public ResultData sync(HttpServletRequest request, HttpServletResponse response) {
        String result = ((HttpRequest)HttpUtil.createPost(this.MS_MSTORE_HTTP + "/store/sync.do").header("ms", "upgrader")).execute().body();
        return (ResultData)JSONObject.parseObject(result, ResultData.class);
    }

    private void reModel(ModelEntity modelParent, String parentIds, int mangerRoleId, List<RoleModelEntity> roleModels, int parentId) {
        modelParent.setModelIsMenu(ObjectUtil.isNotNull(modelParent.getModelChildList()) && modelParent.getModelChildList().size() > 0 ? 1 : 0);
        modelParent.setModelId(parentId);
        modelParent.setModelDatetime(new Timestamp(System.currentTimeMillis()));
        modelParent.setModelParentIds(parentIds);
        ModelEntity modelParentEntity = this.modelBiz.getEntityByModelCode(modelParent.getModelCode());
        if (modelParentEntity == null) {
            this.modelBiz.saveEntity(modelParent);
            RoleModelEntity roleModel = new RoleModelEntity();
            roleModel.setRoleId(mangerRoleId);
            roleModel.setModelId(modelParent.getIntId());
            roleModels.add(roleModel);
        } else {
            modelParent.setIntId(modelParentEntity.getModelId());
            this.modelBiz.updateEntity(modelParent);
        }

        if (ObjectUtil.isNotNull(modelParent.getModelChildList()) && modelParent.getModelChildList().size() > 0) {
            Iterator var9 = modelParent.getModelChildList().iterator();

            while(var9.hasNext()) {
                ModelEntity modelEntity = (ModelEntity)var9.next();
                this.reModel(modelEntity, StringUtils.isBlank(parentIds) ? modelParent.getId() : parentIds + "," + modelParent.getId(), mangerRoleId, roleModels, modelParent.getIntId());
            }
        }

    }

    @RequestMapping({"/setup"})
    public String setup(HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
        boolean dataInit = true;
        String token = BasicUtil.getString("token");
        String id = BasicUtil.getString("id");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = this.MS_MSTORE_HTTP + "/store/client/" + id + "/setup.do";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Host", this.MS_MSTORE_HOST);
        httpPost.setHeader("ms", "upgrader");
        httpPost.setHeader("accept", "");
        httpPost.setHeader("method", "setup");
        httpPost.setHeader("token", token);
        httpPost.setHeader("Content-Type", "text/html;charset=UTF-8");
        CloseableHttpResponse httpResponse = null;

        try {
            httpResponse = httpclient.execute(httpPost);
        } catch (HttpHostConnectException var80) {
            var80.printStackTrace();
            ResultData result = ResultData.build().error().msg("链接MStore失败，请回到主界面重试！");
            request.setAttribute("result", result);
            return "/store-client/error";
        }

        HttpEntity entity = httpResponse.getEntity();
        InputStream in = entity.getContent();
        String zipfile = "";
        String source = "";
        String menuStr = "";

        try {
            zipfile = httpResponse.getHeaders("fileName")[0].getValue();
            source = httpResponse.getHeaders("source")[0].getValue();
        } catch (ArrayIndexOutOfBoundsException var79) {
            ResultData result = ResultData.build().error().msg("安装包获取失败，请回到主界面重试！");
            request.setAttribute("result", result);
            return "/store-client/error";
        }

        if (StringUtil.isBlank(zipfile)) {
            ResultData result = ResultData.build().error().msg("安装包获取失败，请回到主界面重试！");
            request.setAttribute("result", result);
            return "/store-client/error";
        } else {
            File dir = FileUtil.file(BasicUtil.getRealPath("temp"));
            if (!FileUtil.exist(dir)) {
                FileUtil.mkdir(dir);
            }

            File zFile = FileUtil.file(dir, zipfile);

            try {
                FileOutputStream fout = new FileOutputStream(zFile);
                //int l = true;
                byte[] tmp = new byte[1024];

                while(true) {
                    int l;
                    if ((l = in.read(tmp)) == -1) {
                        fout.flush();
                        fout.close();
                        break;
                    }

                    fout.write(tmp, 0, l);
                }
            } finally {
                in.close();
            }

            httpclient.close();
            String entryName = "";
            ArrayList initFiles = new ArrayList();
            ArrayList ddlSqls = new ArrayList();
            ArrayList dataSqls = new ArrayList();
            new ArrayList();
            ArrayList classFiles = new ArrayList();
            File menu = null;

            try {
                ZipFile zipFile = new ZipFile(zFile);
                Enumeration zipEnum = zipFile.getEntries();

                label686:
                while(true) {
                    while(true) {
                        ZipEntry entry;
                        while(true) {
                            if (!zipEnum.hasMoreElements()) {
                                zipFile.close();
                                break label686;
                            }

                            entry = (ZipEntry)zipEnum.nextElement();

                            try {
                                entryName = new String(entry.getName().getBytes("utf-8"));
                                if (entryName.indexOf(".DS_Store") <= -1 && entryName.indexOf("__MACOSX") <= -1) {
                                    if (entryName.contains(File.separator)) {
                                        FileUtil.mkdir(entryName.substring(0, entryName.indexOf(File.separator)));
                                    }
                                    break;
                                }
                            } catch (UnsupportedEncodingException var86) {
                                var86.printStackTrace();
                                break;
                            }
                        }

                        if (entry.isDirectory()) {
                            (new File(dir.getAbsolutePath() + File.separator + entryName)).mkdirs();
                        } else {
                            try {
                                File temp = new File(dir.getAbsolutePath() + File.separator + entryName);
                                InputStream input = zipFile.getInputStream(entry);
                                OutputStream output = new FileOutputStream(temp);
                                if (entryName.indexOf(".sql") > 0) {
                                    if (entryName.indexOf("ddl.sql") > 0) {
                                        ddlSqls.add(new File(dir.getAbsolutePath() + File.separator + entryName));
                                    } else if (entryName.indexOf("data.sql") > 0) {
                                        dataSqls.add(new File(dir.getAbsolutePath() + File.separator + entryName));
                                    } else {
                                        initFiles.add(new File(dir.getAbsolutePath() + File.separator + entryName));
                                    }
                                }

                                if (entryName.indexOf(".class") > 0) {
                                    classFiles.add(new File(dir.getAbsolutePath() + File.separator + entryName));
                                }

                                if (entryName.indexOf(".json") > 0) {
                                    menu = new File(dir.getAbsolutePath() + File.separator + entryName);
                                }

                                byte[] buffer = new byte[8192];
                                boolean var31 = false;

                                int readLen;
                                while((readLen = input.read(buffer, 0, 8192)) != -1) {
                                    output.write(buffer, 0, readLen);
                                }

                                output.flush();
                                output.close();
                                input.close();
                                input = null;
                                output = null;
                            } catch (IOException var87) {
                                var87.printStackTrace();
                            }
                        }
                    }
                }
            } catch (ZipException var88) {
                var88.printStackTrace();
            } catch (IOException var89) {
                var89.printStackTrace();
            }

            Connection conn = null;
            if (initFiles.size() > 0 || ddlSqls.size() > 0 || dataSqls.size() > 0) {
                try {
                    conn = this.dataSource.getConnection();
                    Iterator var99 = ddlSqls.iterator();

                    File sql;
                    while(var99.hasNext()) {
                        sql = (File)var99.next();
                        Resource resource = new UrlResource(sql.toURI());
                        ScriptUtils.executeSqlScript(conn, resource);
                    }

                    var99 = initFiles.iterator();

                    String sqlStr;
                    UrlResource resource;
                    while(var99.hasNext()) {
                        sql = (File)var99.next();
                        sqlStr = FileUtil.readUtf8String(sql);
                        if (StringUtils.isNotBlank(sqlStr)) {
                            FileUtil.writeUtf8String(sqlStr, sql);
                            resource = new UrlResource(sql.toURI());

                            try {
                                ScriptUtils.executeSqlScript(conn, resource);
                            } catch (Exception var78) {
                                var78.printStackTrace();
                            }
                        }
                    }

                    if (dataInit) {
                        var99 = dataSqls.iterator();

                        while(var99.hasNext()) {
                            sql = (File)var99.next();
                            sqlStr = FileUtil.readUtf8String(sql);
                            if (StringUtils.isNotBlank(sqlStr)) {
                                FileUtil.writeUtf8String(sqlStr, sql);
                                resource = new UrlResource(sql.toURI());

                                try {
                                    ScriptUtils.executeSqlScript(conn, resource);
                                } catch (Exception var77) {
                                    var77.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (SQLException var84) {
                    var84.printStackTrace();
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException var74) {
                        var74.printStackTrace();
                    }

                }
            }

            if (menu != null) {
                menuStr = FileUtil.readUtf8String(menu);
            }

            int mangerRoleId = this.getManagerBySession().getRoleId();
            this.modelBiz.jsonToModel(menuStr, mangerRoleId, 0);
            if (classFiles.size() > 0) {
                URLClassLoader cload = new URLClassLoader(new URL[]{dir.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

                try {
                    Iterator var107 = classFiles.iterator();

                    while(var107.hasNext()) {
                        File cls = (File)var107.next();
                        String className = cls.getPath().substring(dir.getPath().length() + 1, cls.getPath().length() - 6);
                        className = className.replace("/", ".").replace("\\", ".");
                        Class clzss = Class.forName(className, true, cload);
                        Object obj = clzss.newInstance();
                        Method[] method = clzss.getDeclaredMethods();
                        Method[] var33 = method;
                        int var34 = method.length;

                        for(int var35 = 0; var35 < var34; ++var35) {
                            Method m = var33[var35];
                            if (m.toString().indexOf("public ") > -1) {
                                try {
                                    m.invoke(obj);
                                } catch (IllegalArgumentException var75) {
                                    var75.printStackTrace();
                                } catch (InvocationTargetException var76) {
                                    var76.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException var81) {
                    var81.printStackTrace();
                } catch (InstantiationException var82) {
                    var82.printStackTrace();
                } catch (IllegalAccessException var83) {
                    var83.printStackTrace();
                }
            }

            FileUtil.del(dir);
            if (StringUtils.isNotBlank(source) && source.equals("yes")) {
                Map map = new HashMap();
                map.put("id", id);
                map.put("token", token);
                ResultData result = ResultData.build().success().msg("安装包获取失败，请回到主界面重试！").data(map);
                request.setAttribute("result", result);
                return "/store-client/success";
            } else {
                ResultData result = ResultData.build().success().msg("安装升级成功！");
                request.setAttribute("result", result);
                return "/store-client/success";
            }
        }
    }

    private boolean checkModel(String className) {
        try {
            Class var2 = Class.forName(className);
            return true;
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return false;
        }
    }
}

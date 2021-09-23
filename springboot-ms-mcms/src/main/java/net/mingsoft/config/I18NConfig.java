//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mingsoft.config;

import freemarker.template.TemplateException;

import java.io.IOException;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class I18NConfig {
    @Autowired
    protected freemarker.template.Configuration configuration;
    @Value("${ms.local.default:zh_CN}")
    private String defaultLocal;
    @Value("$ms.local.language:zh_CN,en_US}")
    private String localLanguage;

    public I18NConfig() {
    }

    @PostConstruct
    public void init() throws IOException, TemplateException {
        this.configuration.setSharedVariable("localDefault", this.defaultLocal);
        this.configuration.setSharedVariable("localLanguage", this.localLanguage.split(","));
    }
}

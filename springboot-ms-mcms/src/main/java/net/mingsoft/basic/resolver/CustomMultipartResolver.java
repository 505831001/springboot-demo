/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.basic.resolver;

import cn.hutool.core.util.ObjectUtil;
import net.mingsoft.base.resolver.MultipartResolver;
import net.mingsoft.config.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自动注入解决百度编辑器的上传组件与CommonsMultipartResolver的冲突问题
 * 不可同时配置 MultipartConfigElement 和 CommonsMultipartResolver
 * @author by Administrator
 * @Description TODO
 * @date 2019/9/29 17:11
 */
@Component
@EnableConfigurationProperties(MultipartProperties.class)
public class CustomMultipartResolver extends MultipartResolver {
    public CustomMultipartResolver(MultipartProperties multipartProperties) throws IOException {
        if(ObjectUtil.isNotNull(multipartProperties.getUploadTempDir())){
            setUploadTempDir(multipartProperties.getUploadTempDir());
        }
        setDefaultEncoding(multipartProperties.getDefaultEncoding());
        setMaxUploadSize(multipartProperties.getMaxFileSize());
        setMaxUploadSizePerFile(multipartProperties.getMaxRequestSize());
        setMaxInMemorySize(multipartProperties.getMaxInMemorySize());
        setResolveLazily(multipartProperties.isResolveLazily());
        setExcludeUrls("jsp/editor.do");
    }


}

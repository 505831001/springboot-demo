package com.liuweiwei.test;

import com.liuweiwei.utils.AESUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AESUtilsTest {

    /**
     * org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
     */
    @Test
    public void contextLoads() {
        /**固定 16 位长度的加密串*/
        String key = "23asdwee2343afwe";
        /**被加密的密文内容*/
        String content = "12345678";
        String enS = AESUtils.encrypt(key, content);
        System.out.println("原始加密：" + enS);
        System.out.println("原始解密：" + AESUtils.decrypt(key, enS));
    }
}

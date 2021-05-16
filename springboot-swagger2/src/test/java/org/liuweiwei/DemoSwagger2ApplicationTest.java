package org.liuweiwei;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoSwagger2ApplicationTest {

    @Test
    public void contextLoads() {
        /**
         * 加密
         */
        String password = "admin";
        String source = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("BCrypt 加密：" + source);
        /**
         * 解密
         */
        boolean target = BCrypt.checkpw(password,"$2a$10$d6oDwJRKYlWQMOKhW/Mi0eNq4xs/utxvfHLeWBUeeXbQLN9vpNhuK");
        System.out.println("BCrypt 解密：" + target);
    }
}

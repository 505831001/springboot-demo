package org.liuweiwei;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoMyBatisApplicationTest {

    @Test
    public void contextLoads() {
        List<Object> list = new ArrayList<>();
        List<List<Object>> partition = Lists.partition(list, 300);
        for (List<Object> objects : partition) {
            for (Object object : objects) {
                System.out.println(object.toString());
            }
        }
        List<List<Object>> partition1 = ListUtils.partition(list, 300);
        for (List<Object> objects : partition1) {
            for (Object object : objects) {
                System.out.println(object.toString());
            }
        }
    }
}

package org.liuweiwei;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.liuweiwei.dao.TbUserMapper;
import org.liuweiwei.model.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoDruidApplicationTest {

    @Autowired
    private TbUserMapper userMapper;

    /**
     * org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
     */
    @Test
    public void contextLoads() {
        List<TbUser> list = userMapper.selectAll();
        for (TbUser user : list) {
            System.out.println(user.toString());
        }
    }
}

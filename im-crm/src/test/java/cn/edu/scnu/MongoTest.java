package cn.edu.scnu;

import cn.edu.scnu.util.FakerUtil;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = IMApplication.class)
public class MongoTest {

    @Autowired
    MongoService mongoService;

    @Test
    void test() {
        String all = mongoService.findAll();
        System.out.println(all);
        mongoService.insert(FakerUtil.randomUser());
        System.out.println(mongoService.findAll());
    }
}

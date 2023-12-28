package cn.edu.scnu;

import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void addTestAccount() {
        UserEntity jam = UserEntity.builder()
            .userId("123456")
            .gender(true)
            .image("https://a.520gexing.com/uploads/allimg/2023082315/lg1o542qkfm1.jpg")
            .description("测试上游账号")
            .username("Jam")
            .password("123456")
            .build();
        UserEntity tom = UserEntity.builder()
            .userId("133333")
            .gender(false)
            .image("https://img.58tg.com/up/allimg/tx29/08151048279757835.jpg")
            .description("测试下游账号")
            .username("Tom")
            .password("123456")
            .build();
        System.out.println(userService.register(jam));
        System.out.println(userService.register(tom));
    }

    @Test
    public void getUser() {
        List<UserEntity> list = userService.list();
        System.out.println(list);
    }
}

package cn.edu.scnu;

import cn.hutool.core.lang.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class ProductTest {
    @Test
    public void testGenerateUUID() {
        String string = UUID.fastUUID()
            .toString();
        System.out.println(string);
    }
}

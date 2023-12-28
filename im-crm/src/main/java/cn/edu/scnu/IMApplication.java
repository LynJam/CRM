package cn.edu.scnu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class IMApplication {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class, args);
    }

    private void test() {
        stringRedisTemplate.opsForValue().set("hello", "world");
    }

    private void test2() {
        mongoTemplate.createCollection("test");
    }
}
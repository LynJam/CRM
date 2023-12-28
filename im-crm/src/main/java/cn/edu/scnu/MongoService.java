package cn.edu.scnu;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoService {
    @Autowired
    private MongoTemplate mongoTemplate;


    public void insert(UserEntity user) {
        mongoTemplate.insert(user);
    }

    public void sayHello() {
        System.out.println("hello");
    }

    public String findAll() {
        List<UserEntity> all = mongoTemplate.findAll(UserEntity.class);
        return all.toString();
    }

}

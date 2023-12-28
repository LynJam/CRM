package cn.edu.scnu.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {
    @Autowired
    private MongoTemplate mongoTemplate;


}

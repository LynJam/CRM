package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Message save(Message msg) {
        Message save = mongoTemplate.save(msg);
        return save;
    }

    public List<Message> findMsgIn(List<String> ids) {
        Query query = Query.query(Criteria.where("messageId")
            .in(ids));
        return mongoTemplate.find(query, Message.class);
    }
}

package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.ReadTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ReadTimeRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateReadTime(String roomId, String userId, String timestamp) {
        Query query = Query.query(Criteria.where("roomId")
            .is(roomId)
            .and("userId")
            .is(userId));
        Update update = Update.update("timestamp", timestamp);
        mongoTemplate.upsert(query, update, ReadTime.class);
    }

    public String findReadTime(String roomId, String userId) {
        Query query = Query.query(Criteria.where("roomId")
            .is(roomId)
            .and("userId")
            .is(userId));
        ReadTime readTime = mongoTemplate.findOne(query, ReadTime.class);
        return readTime != null ? readTime.getTimestamp() : "0";
    }
}

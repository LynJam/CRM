package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.TimelineSynchroNode;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class TimelineSynchroNodeRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public TimelineSynchroNode find(String userId, String deviceType) {
        Query query = new Query(Criteria.where("userId").is(userId).and("deviceType").is(deviceType));
        return mongoTemplate.findOne(query, TimelineSynchroNode.class);
    }

    public void update(String userId, String deviceType, ObjectId lastTimelineId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("deviceType").is(deviceType));
        Update update = new Update().set("lastTimelineId", lastTimelineId).set("lastPullTimestamp", String.valueOf(System.currentTimeMillis()));
        mongoTemplate.updateFirst(query, update, TimelineSynchroNode.class);
    }

    public void save(String userId, String deviceType, ObjectId lastTimelineId) {
        TimelineSynchroNode node = TimelineSynchroNode.builder()
            .userId(userId)
            .deviceType(deviceType)
            .lastTimelineId(lastTimelineId)
            .build();
        mongoTemplate.save(node);
    }
}

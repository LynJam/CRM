package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.Timeline;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class TimelineRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    private static final String USER_TL_PREFIX = "TLU_";
    private static final String ROOM_TL_PREFIX = "TLR_";

    public void writeUserTimelines(String messageId, List<String> userIds) {
        Timeline timeline = Timeline.of(messageId);
        userIds.forEach(userId -> {
            mongoTemplate.save(timeline, USER_TL_PREFIX + userId);
        });
    }

    public void writeRoomTimeline(String messageId, String roomId) {
        Timeline save = mongoTemplate.save(Timeline.of(messageId), ROOM_TL_PREFIX + roomId);
    }

    public List<Timeline> findTimelinesGreaterThanId(ObjectId id, String userId) {
        Query query = new Query(Criteria.where("id")
            .gt(id)).with(Sort.by(Direction.ASC, "id"));
        return mongoTemplate.find(query, Timeline.class, USER_TL_PREFIX + userId);
    }

    public List<Timeline> findHistoricalRecord(ObjectId id, String roomId, int size) {
        Query query = new Query(Criteria.where("id")
            .lt(id)).with(Sort.by(Sort.Direction.DESC, "id"))
            .limit(size);
        return mongoTemplate.find(query, Timeline.class, ROOM_TL_PREFIX + roomId);
    }

    public List<Timeline> findHistoricalRecord(String roomId, int size) {
        Query query = new Query().with(Sort.by(Direction.DESC, "id"))
            .limit(size);
        return mongoTemplate.find(query, Timeline.class, ROOM_TL_PREFIX + roomId);
    }

    public Timeline findUserTimelineByMsgId(String msgId, String userId) {
        Query query = new Query(Criteria.where("messageId")
            .is(msgId));
        return mongoTemplate.findOne(query, Timeline.class, USER_TL_PREFIX + userId);
    }

    public Timeline findRoomTimelineByMsgId(String msgId, String roomId) {
        Query query = new Query(Criteria.where("messageId")
            .is(msgId));
        return mongoTemplate.findOne(query, Timeline.class, ROOM_TL_PREFIX + roomId);
    }
}

package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.User;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public User findUserByUserId(String userId) {
        Query query = Query.query(Criteria.where("userId")
            .is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    public List<User> findUsersWithoutRoomIds(Collection<String> userIds) {
        Query query = Query.query(Criteria.where("userId")
            .in(userIds));
        query.fields()
            .exclude("roomIds");
        return mongoTemplate.find(query, User.class);
    }

    public void addUser(User user) {
        mongoTemplate.save(user);
    }

    public void addRoomIdsToUsers(List<String> userIds, List<String> roomIds) {
        Query query = Query.query(Criteria.where("userId").in(userIds));
        Update update = new Update().addToSet("roomIds").each(roomIds.toArray());
        mongoTemplate.updateMulti(query, update, User.class);
    }

    public void removeRoomIdsFromUsers(List<String> userIds, List<String> roomIds) {
        Query query = Query.query(Criteria.where("userId").in(userIds));
        Update update = new Update().pullAll("roomIds", roomIds.toArray());
        mongoTemplate.updateMulti(query, update, User.class);
    }

    public void addRoomIdsToUser(String userId, List<String> roomIds) {
        Query query = Query.query(Criteria.where("userId")
            .is(userId));
        Update update = new Update().addToSet("roomIds")
            .each(roomIds.toArray());
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void removeRoomIdsFromUser(String userId, List<String> roomIds) {
        Query query = Query.query(Criteria.where("userId")
            .is(userId));
        Update update = new Update().pullAll("roomIds", roomIds.toArray());
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void updateUser(User user) {
        Query query = Query.query(Criteria.where("userId").is(user.getUserId()));
        Update update = new Update();

        if (user.getUsername() != null) {
            update.set("username", user.getUsername());
        }
        if (user.getAvatar() != null) {
            update.set("avatar", user.getAvatar());
        }

        mongoTemplate.updateFirst(query, update, User.class);
    }
}

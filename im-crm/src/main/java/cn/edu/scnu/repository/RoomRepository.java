package cn.edu.scnu.repository;

import cn.edu.scnu.pojo.Room;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Room> findAllById(List<String> roomIds) {
        Query query = Query.query(Criteria.where("roomId")
            .in(roomIds));
        return mongoTemplate.find(query, Room.class);
    }

    public Room addRoom(Room room) {
        return mongoTemplate.save(room);
    }

    public void updateRoom(Room room) {
        Query query = Query.query(Criteria.where("roomId")
            .is(room.getRoomId()));
        Update update = new Update();
        if(room.getRoomName() != null) {
            update.set("roomName", room.getRoomName());
        }
        if(room.getAvatar() != null) {
            update.set("avatar", room.getAvatar());
        }
        mongoTemplate.updateFirst(query, update, Room.class);
    }
}

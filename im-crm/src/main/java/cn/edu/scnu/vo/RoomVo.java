package cn.edu.scnu.vo;

import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.pojo.User;
import java.util.List;
import lombok.Data;

@Data
public class RoomVo {

    private String roomId;

    // 群聊的话(userIds.size()>2)，需定义一个群聊的头像和名称。否则：头像和名称存储为null, 使用另一个用户的头像和名称。
    private String roomName;
    private String avatar;
    private List<User> users;

    public boolean isSingleChat() {
        return users.size() == 2;
    }

    public void setRoomInfo(String userId) {
        if(!isSingleChat() ) {
            return;
        }
        User target = userId.equals(users.get(0).getUserId()) ? users.get(0) : users.get(1);
        setRoomName(target.getUsername());
        setAvatar(target.getAvatar());
    }

    public static RoomVo of(Room room) {
        RoomVo roomVo = new RoomVo();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setRoomName(room.getRoomName());
        roomVo.setAvatar(room.getAvatar());
        return roomVo;
    }

    public static RoomVo of(Room room, List<User> users, String userId) {
        RoomVo of = of(room);
        of.setUsers(users);
        of.setRoomInfo(userId);
        return of;
    }
}

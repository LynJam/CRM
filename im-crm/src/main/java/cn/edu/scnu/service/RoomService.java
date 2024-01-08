package cn.edu.scnu.service;

import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.vo.RoomVo;
import java.util.List;

public interface RoomService {
    public List<RoomVo> findRoomsWithUserId(String userId);

    public List<RoomVo> findRoomsWithUser(List<String> roomIds, String userId);

    public Room addNewRoom(Room room);

    public Room findRoomByRoomId(String roomId);
}

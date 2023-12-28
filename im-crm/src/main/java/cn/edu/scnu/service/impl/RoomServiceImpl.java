package cn.edu.scnu.service.impl;

import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.pojo.User;
import cn.edu.scnu.repository.RoomRepository;
import cn.edu.scnu.repository.UserRepository;
import cn.edu.scnu.service.RoomService;
import cn.edu.scnu.vo.RoomVo;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RoomVo> findRoomsWithUserId(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return findRoomsWithUser(user.getRoomIds(), user.getUserId());
    }

    @Override
    public List<RoomVo> findRoomsWithUser(List<String> roomIds, String userId) {
        List<Room> rooms = roomRepository.findAllById(roomIds);

        Set<String> allUserIds = rooms.stream()
            .flatMap(room -> room.getUserIds()
                .stream())
            .collect(Collectors.toSet());
        List<User> users = userRepository.findUsersWithoutRoomIds(allUserIds);
        Map<String, User> userMap = users.stream()
            .collect(Collectors.toMap(User::getUserId, Function.identity()));

        ArrayList<RoomVo> roomVos = Lists.newArrayList();
        rooms.forEach(room -> {
            List<User> roomUsers = room.getUserIds()
                .stream()
                .map(userMap::get)
                .collect(Collectors.toList());
            RoomVo roomVo = RoomVo.of(room, roomUsers, userId);
            roomVos.add(roomVo);
        });

        return roomVos;
    }

    @Override
    public Room addNewRoom(Room room) {
        Room r = roomRepository.addRoom(room);
        String roomId = r.getRoomId();
        List<String> userIds = r.getUserIds();
        userRepository.addRoomIdsToUsers(userIds, Lists.newArrayList(roomId));
        return r;
    }
}

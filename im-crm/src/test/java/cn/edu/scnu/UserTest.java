package cn.edu.scnu;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.pojo.User;
import cn.edu.scnu.pojo.User.UserBuilder;
import cn.edu.scnu.service.RoomService;
import cn.edu.scnu.service.UserService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.RoomVo;
import cn.edu.scnu.ws.WsProtocol;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest {
    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Test
    public void addTestUser() {
        //getUsers().stream().forEach(u -> {
        //    userService.addNewUser(u);
        //});
        User system = User.builder()
            .username("SYSTEM")
            .userId("000000")
            .build();
        userService.addNewUser(system);
    }
    @Test
    public void addTestRoom() {
        List<String> ids = getUsers().stream()
            .map(User::getUserId)
            .collect(Collectors.toList());
        Room room1 = Room.builder()
            .userIds(Lists.newArrayList(ids.get(0), ids.get(1)))
            .build();
        Room room2 = Room.builder()
            .userIds(Lists.newArrayList(ids.get(0), ids.get(2)))
            .build();

        Room room = roomService.addNewRoom(room1);
        roomService.addNewRoom(room2);
    }

    public List<User> getUsers() {

        User jam = User.builder()
            .userId("123456")
            .avatar("https://a.520gexing.com/uploads/allimg/2023082315/lg1o542qkfm1.jpg")
            .username("Jam")
            .roomIds(Lists.newArrayList())
            .build();
        User tom = User.builder()
            .userId("133333")
            .avatar("https://img.58tg.com/up/allimg/tx29/08151048279757835.jpg")
            .username("Tom")
            .roomIds(Lists.newArrayList())
            .build();

        User mary = User.builder()
            .userId("144444")
            .username("Mary")
            .avatar("https://a.520gexing.com/uploads/allimg/2018032907/afdmquwjhrc.jpg")
            .build();

        return Lists.newArrayList(jam, tom, mary);
    }

    @Test
    public void testGetRooms() {
        List<User> users = getUsers();
        List<RoomVo> roomsWithUserId = roomService.findRoomsWithUserId(users.get(0)
            .getUserId());
        String jsonStr = JsonUtil.toJson(roomsWithUserId);
        System.out.println(jsonStr );
    }

    @Test
    public void testEncode() {

        List<RoomVo> data = roomService.findRoomsWithUserId("123456");
        WsProtocol<List<RoomVo>> wsProtocol = WsProtocol.of(ProtocolTypeEnum.GET_ROOMS.type, data);
        String json = JsonUtil.toJson(wsProtocol);

        WsProtocol deData = JsonUtil.toObject(json, WsProtocol.class);

    }


}

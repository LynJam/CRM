package cn.edu.scnu.service.impl;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.repository.ReadTimeRepository;
import cn.edu.scnu.repository.RoomRepository;
import cn.edu.scnu.service.ReadMessageService;
import cn.edu.scnu.ws.WebSocketChannel;
import cn.edu.scnu.ws.WsProtocol;
import cn.edu.scnu.ws.handler.WsParamModel.ReadMessageRes;
import java.util.List;
import java.util.Map;
import javax.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadMessageServiceImpl implements ReadMessageService {
    @Autowired
    private ReadTimeRepository readTimeRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void readMessage(String roomId, String userId, String timestamp) {
        readTimeRepository.updateReadTime(roomId, userId, timestamp);

        Map<String, Session> onlineUser = WebSocketChannel.getOnlineUser();
        Room room = roomRepository.findOne(roomId);
        List<String> userIds = room.getUserIds();
        ReadMessageRes data = ReadMessageRes.builder()
            .timestamp(timestamp)
            .roomId(roomId)
            .userId(userId)
            .build();
        WsProtocol<ReadMessageRes> res = WsProtocol.of(ProtocolTypeEnum.READ_MESSAGES_RESP.type, data);
        userIds.forEach(uid -> {
            if (userId.equals(uid)) {
                return;
            }
            // TODO 若在线，更新消息
            WebSocketChannel.sendObject(uid, res);
        });
    }
}

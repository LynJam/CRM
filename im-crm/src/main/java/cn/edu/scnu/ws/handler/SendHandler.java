package cn.edu.scnu.ws.handler;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.pojo.Message;
import cn.edu.scnu.pojo.Room;
import cn.edu.scnu.service.MessageService;
import cn.edu.scnu.service.RoomService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.ws.WebSocketChannel;
import cn.edu.scnu.ws.WsProtocol;
import cn.edu.scnu.ws.handler.WsParamModel.SendArg;
import cn.edu.scnu.ws.handler.WsParamModel.SendRes;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import javax.websocket.Session;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendHandler implements WsHandler {
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoomService roomService;

    @Override
    public WsProtocol handler(WsProtocol<JsonNode> wsProtocol) {
        SendArg arg = JsonUtil.toObject(wsProtocol.getData()
            .toString(), SendArg.class);
        Room room = roomService.findRoomByRoomId(arg.getRoomId());
        List<String> userIds = room.getUserIds();
        Message msg = Message.builder()
            .senderId(arg.getSenderId())
            .content(arg.getContent())
            .timestamp(arg.getTimestamp())
            .roomId(arg.getRoomId())
            .build();
        Message saveMsg = messageService.sendMessage(msg, arg.getRoomId(), userIds, arg.getDeviceType());

        // TODO 若接收者在线，异步通知接收者
        userIds.forEach(userId -> {
            if (userId.equals(arg.getSenderId())) {
                return;
            }
            if (!isUserOnline(userId)) {// 判断用户是否在线
                return;
            }
            sendNotice(userId);
        });

        // 返回服务器的 messageId 和原本客户端传来的 id
        SendRes res = SendRes.builder()
            .frontMsgId(arg.getId())
            .serviceId(saveMsg.getMessageId())
            .roomId(arg.getRoomId())
            .build();
        return WsProtocol.of(ProtocolTypeEnum.SEND_ACK.type, res);
    }

    private boolean isUserOnline(String userId) {
        // TODO isUserOnline
        return true;
    }

    private void sendNotice(String userId) {
        Session session = WebSocketChannel.getOnlineUser()
            .get(userId);
        if (ObjectUtils.isEmpty(session)) {
            return;
        }
        WsProtocol<Object> data = WsProtocol.of(ProtocolTypeEnum.NEW_MSG_NOTICE.type, null);
        session.getAsyncRemote()
            .sendObject(data);
    }
}

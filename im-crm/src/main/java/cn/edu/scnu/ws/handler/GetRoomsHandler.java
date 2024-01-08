package cn.edu.scnu.ws.handler;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.service.RoomService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.RoomVo;
import cn.edu.scnu.ws.WsProtocol;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetRoomsHandler implements WsHandler{
    @Autowired
    private RoomService roomService;

    @Override
    public WsProtocol<?> handler(WsProtocol<JsonNode> protocol) {
        String arg = JsonUtil.toObject(protocol.getData()
            .toString(), String.class);
        return handler(arg);
    }

    private WsProtocol<?> handler(String userId) {
        List<RoomVo> roomVos = roomService.findRoomsWithUserId(userId);
        return WsProtocol.of(ProtocolTypeEnum.RESP_ROOMS.type, roomVos);
    }
}

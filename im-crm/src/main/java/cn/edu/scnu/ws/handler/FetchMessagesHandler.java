package cn.edu.scnu.ws.handler;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.service.MessageService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.MessageVo;
import cn.edu.scnu.ws.WsProtocol;
import cn.edu.scnu.ws.handler.WsParamModel.FetchMessageArg;
import cn.edu.scnu.ws.handler.WsParamModel.FetchMessageRes;
import cn.edu.scnu.ws.handler.WsParamModel.FetchMessageRes.FetchMessageResBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FetchMessagesHandler implements WsHandler {
    @Autowired
    private MessageService messageService;

    @Override
    public WsProtocol handler(WsProtocol<JsonNode> protocol) {
        FetchMessageArg arg = JsonUtil.toObject(protocol.getData()
            .toString(), FetchMessageArg.class);
        List<MessageVo> messages = messageService.fetchRoomHistoricalMsg(arg.getOldestClientMessageId(), arg.getRoomId(), arg.getSize());
        if (messages.isEmpty()) {
            log.warn("{} 房间已经拉取过所有历史消息了！", arg.getRoomId());
        }
        FetchMessageRes data = FetchMessageRes.builder()
            .messages(messages)
            .roomId(arg.getRoomId())
            .noHistoricalMsg(messages.isEmpty())
            .build();
        return WsProtocol.of(ProtocolTypeEnum.RESP_OLD_MESSAGES.type, data);
    }
}

package cn.edu.scnu.ws.handler;

import cn.edu.scnu.service.ReadMessageService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.ws.WsProtocol;
import cn.edu.scnu.ws.handler.WsParamModel.ReadMessageArg;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReadMessageHandler implements WsHandler {
    @Autowired
    private ReadMessageService readMessageService;

    @Override
    public WsProtocol handler(WsProtocol<JsonNode> protocol) {
        ReadMessageArg arg = JsonUtil.toObject(protocol.getData()
            .toString(), ReadMessageArg.class);
        readMessageService.readMessage(arg.getRoomId(), arg.getUserId(), arg.getTimestamp());
        return null;
    }
}

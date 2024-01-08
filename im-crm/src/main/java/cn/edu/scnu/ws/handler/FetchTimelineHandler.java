package cn.edu.scnu.ws.handler;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.pojo.Message;
import cn.edu.scnu.service.MessageService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.MessageVo;
import cn.edu.scnu.ws.WsProtocol;
import cn.edu.scnu.ws.handler.WsParamModel.FetchTimelineArg;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FetchTimelineHandler implements WsHandler {
    @Autowired
    private MessageService messageService;
    @Override
    public WsProtocol handler(WsProtocol<JsonNode> protocol) {
        FetchTimelineArg arg = JsonUtil.toObject(protocol.getData().toString(), FetchTimelineArg.class);
        log.info(arg.toString());
        Map<String, List<MessageVo>> newMsg = messageService.fetchUserNewMsg(arg.getUserId(), arg.getDeviceType());
        if(newMsg == null) {
            log.info("userId: {}, 在 {} 设备，消息已是最新！", arg.getUserId(), arg.getDeviceType());
            return null;
        }
        WsProtocol<Map<String, List<MessageVo>>> of = WsProtocol.of(ProtocolTypeEnum.RESP_TIMELINE.type, newMsg);
        return of;
    }
}

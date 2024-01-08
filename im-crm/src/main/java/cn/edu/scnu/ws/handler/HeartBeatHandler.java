package cn.edu.scnu.ws.handler;

import cn.edu.scnu.ws.WsProtocol;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartBeatHandler implements WsHandler{
    @Override
    public WsProtocol handler(WsProtocol<JsonNode> protocol) {
        log.info("[websocket] 收到心跳 {}", protocol);
        return null;
    }
}

package cn.edu.scnu.ws.handler;

import cn.edu.scnu.ws.WsProtocol;
import com.fasterxml.jackson.databind.JsonNode;

public interface WsHandler {
    public WsProtocol handler(WsProtocol<JsonNode> protocol);
}

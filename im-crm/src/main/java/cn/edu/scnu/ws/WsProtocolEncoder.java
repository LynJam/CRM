package cn.edu.scnu.ws;

import cn.edu.scnu.util.JsonUtil;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WsProtocolEncoder implements Encoder.Text<WsProtocol<?>>{
    @Override
    public String encode(WsProtocol<?> msg) throws EncodeException {
        return JsonUtil.toJson(msg);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

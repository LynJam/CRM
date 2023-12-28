package cn.edu.scnu.ws;

import cn.hutool.json.JSONUtil;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<WsMessage<?>>{
    @Override
    public String encode(WsMessage<?> msg) throws EncodeException {
        return JSONUtil.toJsonStr(msg);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

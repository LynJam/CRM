package cn.edu.scnu.ws;

import cn.edu.scnu.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WsProtocolDecoder implements Decoder.Text<WsProtocol<JsonNode>> {
    @Override
    public WsProtocol<JsonNode> decode(String s) throws DecodeException {
        try {
            return JsonUtil.toObject(s, new TypeReference<WsProtocol<JsonNode>>() {
            }); // data 保存在 String 类型的字符串，到 type 对应的 WsHandler 再进行转换成具体对象
        } catch (Exception e) {
            throw new DecodeException(s, "Error decoding JSON to WsProtocol", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

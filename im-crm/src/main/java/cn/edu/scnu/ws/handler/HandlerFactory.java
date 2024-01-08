package cn.edu.scnu.ws.handler;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.exception.ValidateException;
import cn.hutool.extra.spring.SpringUtil;
import java.util.EnumMap;
import java.util.Map;

public class HandlerFactory {
    private static final Map<ProtocolTypeEnum, Class<? extends WsHandler>>  HANDLER_MAP = new EnumMap<>(ProtocolTypeEnum.class);

    static {
        for (ProtocolTypeEnum protocolTypeEnum : ProtocolTypeEnum.values()) {
            HANDLER_MAP.put(protocolTypeEnum, protocolTypeEnum.getHandlerClass());
        }
    }

    public static WsHandler getHandler(Integer type) {
        ProtocolTypeEnum protocolType = ProtocolTypeEnum.fromType(type);
        if (protocolType != null) {
            Class<? extends WsHandler> handlerClass = HANDLER_MAP.get(protocolType);
            if (handlerClass != null) {
                return SpringUtil.getBean(handlerClass);
            }
        }
        throw new ValidateException("WsProtocol 中的 type 类型不合法: " + type);
    }
}

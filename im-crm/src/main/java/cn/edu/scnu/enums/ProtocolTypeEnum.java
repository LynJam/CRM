package cn.edu.scnu.enums;

import cn.edu.scnu.ws.handler.FetchMessagesHandler;
import cn.edu.scnu.ws.handler.FetchTimelineHandler;
import cn.edu.scnu.ws.handler.GetRoomsHandler;
import cn.edu.scnu.ws.handler.HeartBeatHandler;
import cn.edu.scnu.ws.handler.ReadMessageHandler;
import cn.edu.scnu.ws.handler.SendHandler;
import cn.edu.scnu.ws.handler.WsHandler;

/**
 * 80000 abcde
 *
 * a位代表：websocket 相关 bc位代表：某模块 de位代表：具体类型，其中奇数代表由服务器推送给客户端，偶数代表由客户端推送给服务器
 */
public enum ProtocolTypeEnum {
    HEART_BEAT(80000, HeartBeatHandler.class),
    SEND(80200, SendHandler.class),
    GET_ROOMS(80100, GetRoomsHandler.class),
    FETCH_MESSAGES(80202, FetchMessagesHandler.class),
    FETCH_TIMELINE(80204, FetchTimelineHandler.class),
    READ_MESSAGES(80208, ReadMessageHandler.class),
    // 返回客户端的类型
    SEND_ACK(80201),
    RESP_ROOMS(80101),
    RESP_OLD_MESSAGES(80203),
    RESP_TIMELINE(80205),

    NEW_MSG_NOTICE(80207),
    READ_MESSAGES_RESP(80209),
    ;

    ProtocolTypeEnum(Integer type) {
        this.type = type;
    }

    ProtocolTypeEnum(Integer type, Class<? extends WsHandler> clz) {
        this.type = type;
        this.handlerClz = clz;
    }

    public final Integer type;
    private Class<? extends WsHandler> handlerClz;

    public Class<? extends WsHandler> getHandlerClass() {
        return handlerClz;
    }

    public static ProtocolTypeEnum fromType(Integer type) {
        for (ProtocolTypeEnum protocolTypeEnum : values()) {
            if (protocolTypeEnum.type.equals(type)) {
                return protocolTypeEnum;
            }
        }
        return null;
    }
}

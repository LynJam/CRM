package cn.edu.scnu.ws;

import cn.edu.scnu.enums.ProtocolTypeEnum;
import cn.edu.scnu.exception.ValidateException;
import cn.edu.scnu.ws.handler.FetchMessagesHandler;
import cn.edu.scnu.ws.handler.FetchTimelineHandler;
import cn.edu.scnu.ws.handler.GetRoomsHandler;
import cn.edu.scnu.ws.handler.HandlerFactory;
import cn.edu.scnu.ws.handler.HeartBeatHandler;
import cn.edu.scnu.ws.handler.SendHandler;
import cn.edu.scnu.ws.handler.WsHandler;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint(value = "/websocket/{userId}", encoders = WsProtocolEncoder.class, decoders = WsProtocolDecoder.class)
@Component
public class WebSocketChannel {
    private static Map<String, Session> onlineUser = new ConcurrentHashMap<>();
    private Session session;
    private String userId;

    @OnMessage
    public void onMessage(WsProtocol<JsonNode> msg) throws IOException {
        log.info("[websocket]用户 {} 发来消息：{}", userId, msg);
        //WsHandler handler = HandlerFactory.getHandler(msg.getType());
        WsHandler handler = getHandler(msg.getType());
        WsProtocol res = handler.handler(msg);
        log.info("res: {}", res);
        if (ObjectUtils.isNotEmpty(res)) {
            sendMsg(res);
        }
    }

    public void sendMsg(WsProtocol ws) {
        this.session.getAsyncRemote()
            .sendObject(ws);
    }

    private WsHandler getHandler(Integer type) {
        if (ProtocolTypeEnum.SEND.type.equals(type)) {
            return SpringUtil.getBean(SendHandler.class);
        } else if (ProtocolTypeEnum.GET_ROOMS.type.equals(type)) {
            return SpringUtil.getBean(GetRoomsHandler.class);
        } else if (ProtocolTypeEnum.HEART_BEAT.type.equals(type)) {
            return SpringUtil.getBean(HeartBeatHandler.class);
        } else if (ProtocolTypeEnum.FETCH_MESSAGES.type.equals(type)) {
            return SpringUtil.getBean(FetchMessagesHandler.class);
        } else if (ProtocolTypeEnum.FETCH_TIMELINE.type.equals(type)) {
            return SpringUtil.getBean(FetchTimelineHandler.class);
        } else {
            throw new ValidateException("WsProtocol 中的 type 类型不合法: " + type);
        }
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.session = session;
        this.userId = userId;
        onlineUser.put(userId, session);
        log.info("[websocket] 用户 {} 建立新的连接：id={}", userId, session.getId());
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        onlineUser.remove(userId);
        log.info("[websocket] 用户 {} 断开连接", userId);
    }

    @OnError
    public void onError(Throwable throwable) throws IOException {
        log.warn("[websocket] userId:{}  id: {}, error: ", userId, session.getId(), throwable);
    }

    public static Map<String, Session> getOnlineUser() {
        return onlineUser;
    }

    public static <T> void sendObject(String userId, WsProtocol<T> res) {
        Session session = onlineUser.get(userId);
        if(session != null) {
            session.getAsyncRemote().sendObject(res);
        } else {
            log.warn("{} 用户当前没有建立 websocket 连接！", userId);
        }
    }
}

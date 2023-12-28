package cn.edu.scnu.ws;

import cn.edu.scnu.MongoService;
import cn.edu.scnu.UserEntity;
import cn.edu.scnu.ws.JsonEncoder;
import cn.edu.scnu.ws.WsMessage;
import cn.hutool.extra.spring.SpringUtil;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint(value = "/websocket/echo", encoders = JsonEncoder.class)
@Component
public class WebSocketChannel {
    private static Map<String, Session> onlineUser = new ConcurrentHashMap<>();

    private MongoService mongoService = SpringUtil.getBean(MongoService.class);

    private Session session;

    @OnMessage
    public void onMessage(String msg) throws IOException {
       log.info("[websocket] 收到消息：{}", msg);
       if("bye".equals(msg)) {
           this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Bye"));
           return;
       }
        UserEntity user = UserEntity.builder().id("123456").name("linzy").age(18).build();
        WsMessage<UserEntity> data = new WsMessage<>();
        data.setData(user);
        data.setType("202");
        this.session.getAsyncRemote().sendObject(data);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //HttpSession httpSession =(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //String userId =(String) httpSession.getAttribute("userId");
        //onlineUser.put(userId, session);
        this.session = session;
        WsMessage<String> msg = new WsMessage<>();
        msg.setType("201");
        msg.setData("hello world");
        this.session.getAsyncRemote().sendObject(msg);
        log.info("[websocket] 新的连接：id={}", session.getId());
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        log.info("[websocket] 断开连接");
    }

    @OnError
    public void onError(Throwable throwable) throws IOException  {
        log.warn("[websocket] id: {}, error: {}", session.getId(), throwable.toString());
    }

}

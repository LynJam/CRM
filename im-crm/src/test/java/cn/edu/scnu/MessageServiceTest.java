package cn.edu.scnu;

import cn.edu.scnu.pojo.Message;
import cn.edu.scnu.pojo.Timeline;
import cn.edu.scnu.repository.TimelineRepository;
import cn.edu.scnu.service.MessageService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.MessageVo;
import cn.edu.scnu.ws.handler.WsParamModel.FetchTimelineArg;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageServiceTest {
    @Autowired
    MessageService messageService;


    @Autowired
    TimelineRepository timelineRepository;


    @Test
    public void test() {
        FetchTimelineArg arg = FetchTimelineArg.builder()
            .userId("123456")
            .deviceType("WEB")
            .build();
        String json = JsonUtil.toJson(arg);
        System.out.println(json);
        FetchTimelineArg arg1 = JsonUtil.toObject(json, FetchTimelineArg.class);
    }

    @Test
    public void testSend() {

        Message msg = Message.builder()
            .messageId("111")
            .content("你们已经成为了客户啦！")
            .roomId("658dbddae011c60e6c6bcb3a")
            .system(true)
            .timestamp(String.valueOf(System.currentTimeMillis()))
            .senderId("000000")
            .build();

        Message msg2 = Message.builder()
            .messageId("112")
            .content("你们已经成为了客户啦！")
            .roomId("658dbddbe011c60e6c6bcb3b")
            .system(true)
            .timestamp(String.valueOf(System.currentTimeMillis()))
            .senderId("000000")
            .build();

       messageService.sendMessage(msg, "658dbddae011c60e6c6bcb3a", Lists.newArrayList("123456", "133333"));
        messageService.sendMessage(msg2, "658dbddbe011c60e6c6bcb3b", Lists.newArrayList("123456", "144444"));
    }

    @Test
    public void testGetHistoricalMsg() {
        List<MessageVo> messageVos = messageService.fetchRoomHistoricalMsg("-1", "658dbddae011c60e6c6bcb3a", 3);
        String json = JsonUtil.toJson(messageVos);
        log.info(json);
    }

    @Test
    public void testTimeline() {
        List<Timeline> timelines = timelineRepository.findHistoricalRecord("658dbddae011c60e6c6bcb3a", 3);
        String json = JsonUtil.toJson(timelines);
        log.info(json);
    }
}

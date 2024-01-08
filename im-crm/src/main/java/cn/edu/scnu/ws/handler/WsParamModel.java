package cn.edu.scnu.ws.handler;

import cn.edu.scnu.vo.MessageVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

public class WsParamModel {
    @Data
    public static class FetchMessageArg {
        private String roomId;
        private String oldestClientMessageId;
        private Integer size;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchMessageRes {
        private List<MessageVo> messages;
        private String roomId;
        private Boolean noHistoricalMsg;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FetchTimelineArg {
        private String userId;
        private String deviceType;
    }

    @Data
    public static class SendArg {
        @JsonProperty("_id")
        private String id;
        private String content;
        private String senderId;
        private String timestamp;
        private String roomId;
        private String deviceType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendRes {
        private String frontMsgId;
        private String serviceId;
        private String roomId;
    }

    @Data
    public static class ReadMessageArg {
        private String roomId;
        private String userId;
        private String timestamp;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadMessageRes {
        private String roomId;
        private String userId;
        private String timestamp;
    }
}

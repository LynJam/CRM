package cn.edu.scnu.vo;

import cn.edu.scnu.pojo.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {

    @JsonProperty("_id")
    private String messageId;
    private String content;
    private String senderId;
    private String timestamp; // 转成能看的时间
    private Boolean system;
    private String date;
    private String avatar;
    private String username;
    private Boolean seen;

    @JsonIgnore
    private String roomId;


    @JsonIgnore
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    @JsonIgnore
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static MessageVo of(Message msg) {
        String timestamp = msg.getTimestamp();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(timestamp)), ZoneId.systemDefault());
        String date = dateTime.format(DATE_FORMATTER);
        String time = dateTime.format(TIME_FORMATTER);
        MessageVo vo = MessageVo.builder()
            .messageId(msg.getMessageId())
            .content(msg.getContent())
            .senderId(msg.getSenderId())
            .timestamp(time)
            .system(msg.getSystem())
            .date(date)
            .roomId(msg.getRoomId())
            .build();
        return vo;
    }
}

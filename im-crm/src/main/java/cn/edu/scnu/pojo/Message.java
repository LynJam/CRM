package cn.edu.scnu.pojo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("messages")
public class Message {
    @Id
    private String messageId;
    private String content;
    private String senderId;
    private String roomId;
    private String timestamp;
    private Boolean system;

}

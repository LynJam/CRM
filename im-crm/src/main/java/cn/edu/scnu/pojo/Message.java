package cn.edu.scnu.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("messages")
public class Message {
    private String messageId;
    private String content;
    private String senderId;
}

package cn.edu.scnu.pojo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * 多集合，每个用户一个信箱集合，每个房间一个信箱集合。
 * TLU+userId
 * TLR+roomId
 */
@Data
@Builder
public class Timeline {
    @Id
    private ObjectId id; // ID 带有时间性质，用户级递增
    private String messageId;


    public static Timeline of(String messageId) {
        return Timeline.builder()
            .messageId(messageId)
            .build();
    }


}

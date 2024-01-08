package cn.edu.scnu.pojo;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("rooms")
public class Room {

    @Id
    private String roomId;

    // 群聊的话(userIds.size()>2)，需定义一个群聊的头像和名称。否则：头像和名称存储为null, 使用另一个用户的头像和名称。
    private String roomName;
    private String avatar;
    private List<String> userIds;

    public boolean isSingleChat() {
        return userIds.size() == 2;
    }

    public String oppositeUserId(String selfUserId) {
        if(isSingleChat()) {
            return userIds.get(0).equals(selfUserId) ? userIds.get(1) : userIds.get(0);
        }
        return "";
    }
}

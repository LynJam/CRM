package cn.edu.scnu.pojo;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("users")
public class User {
    @Id
    private String userId;
    private String username;
    private String avatar;
    private List<String> roomIds;
}

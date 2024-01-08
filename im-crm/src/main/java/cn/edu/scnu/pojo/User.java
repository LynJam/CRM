package cn.edu.scnu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("_id")
    private String userId;
    private String username;
    private String avatar;
    @JsonIgnore
    private List<String> roomIds;
}

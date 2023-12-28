package cn.edu.scnu;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@ToString
@Document("User")
public class UserEntity {
    @Id
    private String id;
    private String name;
    private Integer age;
}

package cn.edu.scnu.pojo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("readTime")
public class ReadTime {
    @Id
    ObjectId id;
    @Indexed
    private String roomId;
    private String userId;
    private String timestamp;
}

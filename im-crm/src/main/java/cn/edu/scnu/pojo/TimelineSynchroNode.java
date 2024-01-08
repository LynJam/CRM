package cn.edu.scnu.pojo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("timelineSynchroNode")
public class TimelineSynchroNode {
    @Id
    private ObjectId id;
    @Indexed
    private String userId;
    private String deviceType;
    private ObjectId lastTimelineId;
    private String lastPullTimestamp;
}

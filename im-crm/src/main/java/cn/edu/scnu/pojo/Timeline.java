package cn.edu.scnu.pojo;

import org.springframework.data.annotation.Id;

/**
 * 多集合，每个用户一个信箱集合，每个房间一个信箱集合。
 * TLU+userId
 * TLR+roomId
 */
public class Timeline {
    @Id
    private String id; // ID 带有时间性质，用户级递增
    private String messageId;
}

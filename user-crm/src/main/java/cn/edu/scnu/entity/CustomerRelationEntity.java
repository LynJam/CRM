package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("customer_relation")
public class CustomerRelationEntity {
    private Integer id;
    private String upUserId;
    private String downUserId;
}


package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer_relation")
public class CustomerRelationEntity {
    @TableId
    private Integer id;
    private String upUserId;
    private String downUserId;
}


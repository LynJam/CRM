package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("add_up_customer_application")
public class AddUpCustomerApplicationEntity {
    @TableId
    String id;
    String applicantId; // 申请者 id，下游 id
    String upUserId;
    String status;
}

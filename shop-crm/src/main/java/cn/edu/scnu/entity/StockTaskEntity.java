package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("stock_task")
public class StockTaskEntity {
    @TableId
    private Integer id;
    private String taskId;
    private String orderId;
    private Date createTime;
    private Integer taskStatus;
}

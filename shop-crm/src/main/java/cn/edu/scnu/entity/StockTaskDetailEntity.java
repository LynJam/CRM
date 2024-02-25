package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stock_task_detail")
public class StockTaskDetailEntity {
    @TableId
    private Integer id;
    private String taskId;
    private String productId;
    private Integer productNum;
    private Integer lockStatus;
}

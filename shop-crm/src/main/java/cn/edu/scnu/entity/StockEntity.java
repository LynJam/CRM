package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stock")
public class StockEntity {
    @TableId
    private Integer id;
    private String productId;
    private Integer stockNum;
    private Integer lockedNum;
}

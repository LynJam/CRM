package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("order_item")
public class OrderItemEntity {
    @TableId
    private Integer id;
    private String orderId;
    private String productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    private Integer quantity;
    private Double realAmount;
}
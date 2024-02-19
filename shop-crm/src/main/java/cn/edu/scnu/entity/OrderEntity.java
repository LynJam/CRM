package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "order")
public class OrderEntity {
    @TableId
    private Integer id;
    private String orderId;
    private String sellerId;
    private String buyerId;
    private Double totalAmount;
    private Double payAmount;
    private Byte status;

    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;

    private Byte confirmStatus;
    private Byte deleteStatus;
    private Date paymentTime;
    private Date deliveryTime;
    private Date receiveTime;
}

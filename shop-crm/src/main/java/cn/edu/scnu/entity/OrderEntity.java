package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "`order`")
public class OrderEntity {
    @TableId
    private Integer id;

    private String orderId;
    private String sellerId;
    private String buyerId;
    private Double totalAmount;
    private Double payAmount;
    /**
     * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
     */
    @TableField(value = "`status`")
    private Byte status;

    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;

    private Date paymentTime;
    private Date deliveryTime;
    private Date receiveTime;

    private Byte confirmStatus;
    private Byte deleteStatus;

}

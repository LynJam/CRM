package cn.edu.scnu.vo;

import lombok.Data;

@Data
public class OrderChangeStatusVo {
    private String orderId;
    private Byte status;
}

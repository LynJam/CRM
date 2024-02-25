package cn.edu.scnu.to.mq;

import lombok.Data;

@Data
public class StockLockTo {
    private String taskId;
    private String orderId;
    private StockDetailTo detailTo;
}

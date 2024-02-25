package cn.edu.scnu.to.mq;

import com.github.javafaker.Stock;
import lombok.Data;

@Data
public class StockDetailTo {
    private String taskId;
    private String productId;
    private Integer productNum;
    private Integer lockStatus;

}

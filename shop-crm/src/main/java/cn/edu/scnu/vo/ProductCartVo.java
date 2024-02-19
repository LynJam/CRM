package cn.edu.scnu.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductCartVo {
    private String productId;
    private String productName;
    private Integer num;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;
}

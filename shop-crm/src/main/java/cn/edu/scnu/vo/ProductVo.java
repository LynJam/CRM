package cn.edu.scnu.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductVo {
    private String productId;
    private String productName;
    private String productImage;
    private String productDesc;
    private String sellerId;
    private BigDecimal productPrice;
    private Integer stockNum;
}

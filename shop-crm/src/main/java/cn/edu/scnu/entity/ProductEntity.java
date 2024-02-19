package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName("product")
public class ProductEntity {
    @TableId
    private Integer id;
    private String productId;
    private String productName;
    private String productImage;
    private String productDesc;
    private String sellerId;
    private BigDecimal productPrice;

    private Boolean deleted;
}

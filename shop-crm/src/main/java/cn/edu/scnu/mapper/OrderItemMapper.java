package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {
    @Insert({"<script>",
        "insert into order_item (order_id, product_id, product_name, product_image, product_price, quantity, real_amount)",
        "values",
        "<foreach collection='list' item='item' index='index' separator=','>",
        "(#{item.orderId}, #{item.productId}, #{item.productName}, #{item.productImage}, #{item.productPrice}, #{item.quantity}, #{item.realAmount})",
        "</foreach>",
        "</script>"})
    int batchInsert(List<OrderItemEntity> orderItemEntities);

}

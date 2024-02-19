package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.ProductEntity;
import cn.edu.scnu.vo.ProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
    @Select("SELECT p.*, s.stock_num FROM product AS p LEFT JOIN stock AS s ON p.product_id = s.product_id WHERE p.seller_id = #{sellerId} AND p.deleted = false")
    List<ProductVo> selectProductsBySellerId(@Param("sellerId") String sellerId);

    @Select({
        "<script>",
        "SELECT p.*, s.stock_num FROM product AS p LEFT JOIN stock AS s ON p.product_id = s.product_id WHERE p.seller_id IN",
        "<foreach collection='upCustomerIdList' item='item' open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        " AND p.deleted = false",
        "</script>"
    })
    List<ProductVo> selectProductsBySellerIds(@Param("upCustomerIdList") List<String> upCustomerIdList);
}

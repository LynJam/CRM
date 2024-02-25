package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.StockEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper extends BaseMapper<StockEntity> {
    /**
     * 锁定库存
     */
    @Update("update stock set lock_stock = lock_stock + #{num} where product_id = #{product_id} and stock - lock_stock >= #{num}")
    Long lockSkuStock(@Param("product_id") String product_id,  @Param("num") Integer num);

    /**
     * 解锁库存
     */
    @Update("update stock set lock_stock = lock_stock - #{num} where product_id = #{product_id}")
    void unlockStock(@Param("product_id") String product_id,  @Param("num") Integer num);
}

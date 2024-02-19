package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.StockEntity;
import cn.edu.scnu.mapper.StockMapper;
import cn.edu.scnu.service.StockService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, StockEntity> implements StockService {
    @Override
    public void updateStock(String productId, Integer stockNum) {
        LambdaQueryWrapper<StockEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StockEntity::getProductId, productId);
        StockEntity stockEntity = new StockEntity();
        stockEntity.setStockNum(stockNum);
        update(stockEntity, queryWrapper);
    }

    @Override
    public void save(String productId, Integer stockNum) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setProductId(productId);
        stockEntity.setStockNum(stockNum);
        save(stockEntity);
    }
}

package cn.edu.scnu.service;

import cn.edu.scnu.entity.StockEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StockService extends IService<StockEntity> {
    void updateStock(String productId, Integer stockNum);

    void save(String productId, Integer stockNum);
}

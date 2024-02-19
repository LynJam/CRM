package cn.edu.scnu.service.impl;

import cn.edu.scnu.constants.RedisKeyPrefix;
import cn.edu.scnu.entity.ProductEntity;
import cn.edu.scnu.mapper.ProductMapper;
import cn.edu.scnu.proxy.CustomerProxy;
import cn.edu.scnu.service.ProductService;
import cn.edu.scnu.service.StockService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.ProductCartVo;
import cn.edu.scnu.vo.ProductVo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductEntity> implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private CustomerProxy customerProxy;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ProductVo> listBySellerId(String sellerId) {
        List<ProductVo> productVos = productMapper.selectProductsBySellerId(sellerId);
        return productVos;
    }

    @Override
    public void del(String productId) {
        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductEntity::getProductId, productId);
        // 逻辑删除，更新deleted字段
        ProductEntity productEntity = new ProductEntity();
        productEntity.setDeleted(true);
        productMapper.update(productEntity, queryWrapper);
    }

    public static void main(String[] args) {
        UUID uuid = UUID.fastUUID();
        System.out.println(uuid.toString());
    }

    @Override
    public void add(ProductVo productVo) {
        // 生成一个 productId
        String uuid = UUID.fastUUID().toString();
        productVo.setProductId(uuid);
        ProductEntity productEntity = new ProductEntity();
        BeanUtil.copyProperties(productVo, productEntity);
        boolean save = save(productEntity);

        stockService.save(uuid, productVo.getStockNum());
    }

    @Transactional
    @Override
    public void updateProduct(ProductVo productVo) {
        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductEntity::getProductId, productVo.getProductId());
        ProductEntity productEntity = new ProductEntity();
        BeanUtil.copyProperties(productVo, productEntity);
        productMapper.update(productEntity, queryWrapper);
        if(productVo.getStockNum() != null) {
            // 更新库存
            stockService.updateStock(productVo.getProductId(), productVo.getStockNum());
        }
    }

    @Override
    public List<ProductVo> shopList(String userId) {
        List<String> upCustomerIdList = customerProxy.getUpCustomerIdList(userId);
        List<ProductVo> productVos = productMapper.selectProductsBySellerIds(upCustomerIdList);
        return productVos;
    }

    @Override
    public List<ProductCartVo> getProductCart(String userId) {
        String json = redisTemplate.opsForValue()
            .get(RedisKeyPrefix.PRODUCT_CART + userId);
        if(StringUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        List<ProductCartVo> data = JsonUtil.toObject(json, new TypeReference<List<ProductCartVo>>() {
        });
        return data;
    }

    @Override
    public void saveProductCart(String userId, List<ProductCartVo> productCartVos) {
        String json = JsonUtil.toJson(productCartVos);
        redisTemplate.opsForValue().set(RedisKeyPrefix.PRODUCT_CART + userId, json);
    }
}

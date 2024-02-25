package cn.edu.scnu.service;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.entity.ProductEntity;
import cn.edu.scnu.vo.OrderVo;
import cn.edu.scnu.vo.ProductCartVo;
import cn.edu.scnu.vo.ProductVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductService extends IService<ProductEntity> {
    List<ProductVo> listBySellerId(String sellerId);

    void del(String productId);

    void add(ProductVo productVo);

    void updateProduct(ProductVo productVo);

    List<ProductVo> shopList(String userId);

    List<ProductCartVo> getProductCart(String userId);

    void saveProductCart(String userId, List<ProductCartVo> productCartVos);

    void delProductCart(String userId);

}

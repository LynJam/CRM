package cn.edu.scnu.controller;

import cn.edu.scnu.dto.RespRes;
import cn.edu.scnu.service.ProductService;
import cn.edu.scnu.vo.ProductCartVo;
import cn.edu.scnu.vo.ProductVo;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public RespRes<List<ProductVo>> list(@RequestParam("sellerId") String sellerId) {
        List<ProductVo> productVos = productService.listBySellerId(sellerId);
        return RespRes.success(productVos);
    }

    @GetMapping("/shopList")
    public RespRes<List<ProductVo>> shopList(@RequestParam("userId") String userId) {
        List<ProductVo> productVos = productService.shopList(userId);
        return RespRes.success(productVos);
    }

    @PostMapping("/update")
    public RespRes<Void> update(@RequestBody ProductVo productVo) {
        // 增量更新
        productService.updateProduct(productVo);
        return RespRes.success(null);
    }

    @GetMapping("/del")
    public RespRes<Void> del(@RequestParam("productId") String productId) {
        productService.del(productId);
        return RespRes.success(null);
    }

    @PostMapping("/add")
    public RespRes<Void> add(@RequestBody ProductVo productVo) {
        productService.add(productVo);
        return RespRes.success(null);
    }


    @GetMapping("/getProductCart")
    public RespRes<List<ProductCartVo>> getProductCart(@RequestParam("userId") String userId) {
        List<ProductCartVo> data = productService.getProductCart(userId);
        return RespRes.success(data);
    }

    @PostMapping("/saveProductCart")
    public RespRes<Void> saveProductCart(@RequestParam("userId") String userId, @RequestBody List<ProductCartVo> productCartVos) {
        productService.saveProductCart(userId, productCartVos);
        return RespRes.success(null);
    }
}

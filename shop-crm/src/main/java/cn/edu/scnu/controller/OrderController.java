package cn.edu.scnu.controller;

import cn.edu.scnu.constants.RedisKeyPrefix;
import cn.edu.scnu.dto.RespRes;
import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.enums.ErrorEnum;
import cn.edu.scnu.service.OrderService;
import cn.edu.scnu.util.JsonUtil;
import cn.edu.scnu.vo.OrderChangeStatusVo;
import cn.edu.scnu.vo.OrderVo;
import cn.edu.scnu.vo.ProductCartVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final static String COMPARE_AND_DEL_SCRIPT = "if redis.call('GET', KEYS[1]) == ARGV[1] then return redis.call('DEL', KEYS[1]) else return 0 end";

    @PostMapping("/toTrade")
    public RespRes<OrderVo> cartToTrade(@RequestBody List<ProductCartVo> productCartVos, @RequestHeader("userId") String userId) {
        OrderVo orderVo = orderService.cartToTrade(productCartVos, userId);
        String token = new UUID(System.currentTimeMillis(), 0L).toString();
        redisTemplate.opsForValue()
            .set(RedisKeyPrefix.ORDER_TOKEN + userId, token);
        orderVo.setToken(token);
        return RespRes.success(orderVo);
    }

    @PostMapping("/submitOrder")
    public RespRes<String> submitOrder(@RequestBody OrderVo orderVo, @RequestHeader("userId") String userId) {
        String token = orderVo.getToken();

        Long executeRes = redisTemplate.execute(new DefaultRedisScript<Long>(COMPARE_AND_DEL_SCRIPT), Lists.newArrayList(RedisKeyPrefix.ORDER_TOKEN + userId), token);
        if (executeRes == 1L) {
            // token 验证成功
            log.info(JsonUtil.toJson(orderVo));
            orderService.submitOrder(orderVo, userId);
            return RespRes.success("ok");
        } else {
            return RespRes.error(ErrorEnum.HAS_EXISTED);
        }
    }

    /**
     * type == 1 ：查询 userId 为卖家的订单，type == 2 ：查询 userId 为买家的订单；else 查询所有订单
     */
    @GetMapping("/list")
    public RespRes<List<OrderVo>> list(@RequestParam("type") int type, @RequestHeader("userId") String userId) {
        List<OrderVo> data = orderService.listByType(type, userId);
        return RespRes.success(data);
    }

    @PostMapping("/changeStatus")
    public RespRes<String> changeStatus(@RequestBody OrderChangeStatusVo statusVo) {
        LambdaQueryWrapper<OrderEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderEntity::getOrderId, statusVo.getOrderId());

        OrderEntity update = new OrderEntity();
        update.setStatus(statusVo.getStatus());
        boolean success = orderService.update(update, lambdaQueryWrapper);
        if (success) {
            return RespRes.success("ok");
        } else {
            return RespRes.error(ErrorEnum.BUSH_EXCEPT);
        }
    }
}

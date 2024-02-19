package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.mapper.OrderMapper;
import cn.edu.scnu.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
}

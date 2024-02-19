package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.OrderItemEntity;
import cn.edu.scnu.mapper.OrderItemMapper;
import cn.edu.scnu.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemEntity> implements OrderItemService {
}

package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.CustomerRelationEntity;
import cn.edu.scnu.mapper.CustomerRelationMapper;
import cn.edu.scnu.service.CustomerRelationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerRelationServiceImpl extends ServiceImpl<CustomerRelationMapper, CustomerRelationEntity> implements CustomerRelationService {
    @Override
    public Boolean havenAddUpUser(String downUserId, String upUserId) {
        LambdaQueryWrapper<CustomerRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerRelationEntity::getUpUserId, upUserId)
            .eq(CustomerRelationEntity::getDownUserId, downUserId).last("limit 1");
        return this.getOne(queryWrapper) != null;

    }

    @Override
    public void save(String applicantId, String upUserId) {
        CustomerRelationEntity customerRelationEntity = new CustomerRelationEntity();
        customerRelationEntity.setDownUserId(applicantId);
        customerRelationEntity.setUpUserId(upUserId);
        this.save(customerRelationEntity);
    }

    @Override
    public List<String> findDownCustomerIds(String userId) {
        LambdaQueryWrapper<CustomerRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerRelationEntity::getUpUserId, userId);
        return this.list(queryWrapper)
            .stream()
            .map(CustomerRelationEntity::getDownUserId)
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<String> findUpCustomerIds(String userId) {
        LambdaQueryWrapper<CustomerRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerRelationEntity::getDownUserId, userId);
        return this.list(queryWrapper)
            .stream()
            .map(CustomerRelationEntity::getUpUserId)
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void remove(String downUserId, String upUserId) {
        LambdaQueryWrapper<CustomerRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerRelationEntity::getDownUserId, downUserId)
            .eq(CustomerRelationEntity::getUpUserId, upUserId);

        // TODO 2021/4/25 删除客户关系后，需要通知申请者, 解散房间
        this.remove(queryWrapper);
    }
}

package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.AddUpCustomerApplicationEntity;
import cn.edu.scnu.mapper.AddUpCustomerApplicationMapper;
import cn.edu.scnu.service.AddUpCustomerApplicationService;
import cn.edu.scnu.vo.CustomerVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AddUpCustomerApplicationServiceImpl extends ServiceImpl<AddUpCustomerApplicationMapper, AddUpCustomerApplicationEntity> implements AddUpCustomerApplicationService {
    @Override
    public List<String> findApplicantIds(String userId) {
        LambdaQueryWrapper<AddUpCustomerApplicationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AddUpCustomerApplicationEntity::getApplicantId)
            .eq(AddUpCustomerApplicationEntity::getUpUserId, userId);
        return this.list(queryWrapper)
            .stream()
            .map(AddUpCustomerApplicationEntity::getApplicantId)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public void remove(String applicantId, String upUserId) {
        LambdaQueryWrapper<AddUpCustomerApplicationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddUpCustomerApplicationEntity::getApplicantId, applicantId)
            .eq(AddUpCustomerApplicationEntity::getUpUserId, upUserId);
        this.remove(queryWrapper);
    }
}

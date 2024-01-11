package cn.edu.scnu.service;

import cn.edu.scnu.entity.CustomerRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface CustomerRelationService extends IService<CustomerRelationEntity> {
    Boolean havenAddUpUser(String downUserId, String upUserId);

    void save(String applicantId, String upUserId);

    List<String> findDownCustomerIds(String userId);

    List<String> findUpCustomerIds(String userId);

    void remove(String downUserId, String upUserId);
}

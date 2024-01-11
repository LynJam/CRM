package cn.edu.scnu.service;

import cn.edu.scnu.entity.AddUpCustomerApplicationEntity;
import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.vo.CustomerVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface AddUpCustomerApplicationService extends IService<AddUpCustomerApplicationEntity> {
    List<String> findApplicantIds(String userId);

    void remove(String applicantId, String upUserId);
}

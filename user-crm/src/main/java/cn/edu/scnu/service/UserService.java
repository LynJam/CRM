package cn.edu.scnu.service;

import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.vo.RefreshTokenVo;
import cn.edu.scnu.vo.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserEntity> {

    UserInfoVo saveUserToken(UserEntity user);

    RefreshTokenVo refreshToken(String userId);

    boolean register(UserEntity user);
}

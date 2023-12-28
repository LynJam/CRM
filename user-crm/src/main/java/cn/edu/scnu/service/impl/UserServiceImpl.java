package cn.edu.scnu.service.impl;

import cn.edu.scnu.constants.RedisKeyPrefix;
import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.mapper.UserMapper;
import cn.edu.scnu.service.UserService;
import cn.edu.scnu.util.EncryptionUtil;
import cn.edu.scnu.util.TokenUtil;
import cn.edu.scnu.vo.RefreshTokenVo;
import cn.edu.scnu.vo.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserInfoVo saveUserToken(UserEntity user) {
        String token = TokenUtil.generateToken(user.getUserId());
        String refreshToken = TokenUtil.generateRefreshToken(user.getUserId());
        redisTemplate.opsForValue()
            .set(RedisKeyPrefix.USER_TOKEN + user.getUserId(), refreshToken, TokenUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        UserInfoVo userInfoVo = UserInfoVo.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .image(user.getImage())
            .token(token)
            .refreshToken(refreshToken)
            .build();
        return userInfoVo;
    }

    @Override
    public RefreshTokenVo refreshToken(String userId) {
        RefreshTokenVo refreshTokenVo = RefreshTokenVo.builder()
            .refreshToken(TokenUtil.generateRefreshToken(userId))
            .token(TokenUtil.generateToken(userId))
            .build();
        redisTemplate.opsForValue()
            .set(RedisKeyPrefix.USER_TOKEN + userId, refreshTokenVo.getRefreshToken(), TokenUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return refreshTokenVo;
    }

    @Override
    public boolean register(UserEntity user) {
        String encryptionPsd = EncryptionUtil.sha(user.getPassword());
        user.setPassword(encryptionPsd);
        return save(user);
    }
}

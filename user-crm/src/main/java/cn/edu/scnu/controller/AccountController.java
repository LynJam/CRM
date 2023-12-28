package cn.edu.scnu.controller;

import cn.edu.scnu.dto.RespRes;
import cn.edu.scnu.util.TokenUtil;
import cn.edu.scnu.vo.RefreshTokenVo;
import cn.edu.scnu.vo.UserLoginVo;
import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.enums.ErrorEnum;
import cn.edu.scnu.service.UserService;
import cn.edu.scnu.util.EncryptionUtil;
import cn.edu.scnu.vo.UserInfoVo;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private UserService userService;

  @RequestMapping("/ping")
  public String ping() {
    return "ping";
  }

  @PostMapping("/login")
  public RespRes login(@RequestBody UserLoginVo userLoginDto) {
    String encryptionPwd = EncryptionUtil.sha(userLoginDto.getPassword());
    UserEntity user = userService.getOne(
        new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, userLoginDto.getUsername())
            .eq(UserEntity::getPassword, encryptionPwd));
    if (ObjectUtil.isNull(user)) {
      return RespRes.error(ErrorEnum.NAME_OR_PWD_ERROR);
    }
    UserInfoVo data = userService.saveUserToken(user);
    return RespRes.success(data);
  }

  @PostMapping("/refreshToken")
  public RespRes refreshToken(@RequestBody Map<String, String> body) {
    String refreshToken = body.get("refreshToken");
    String userId = body.getOrDefault("userId", "");
    boolean valid = userId.equals(TokenUtil.getUserIdByRefreshToken(refreshToken));
    if(!valid) {
      return RespRes.errorOfLoginExpired();
    }
    RefreshTokenVo data = userService.refreshToken(userId);
    return RespRes.success(data);
  }

  @PostMapping("/register")
  public String register() {
    return "";
  }
}

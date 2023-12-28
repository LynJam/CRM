package cn.edu.scnu.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoVo {
    private String userId;
    private String username;
    private String image;
    private String token;
    private String refreshToken;
}

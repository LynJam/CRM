package cn.edu.scnu.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenVo {
    private String token;
    private String refreshToken;
}

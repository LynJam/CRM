package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("user")
public class UserEntity {
    private String userId;
    private String username;
    private String password;
    private String image;
    private Boolean gender;
    private String description;
}


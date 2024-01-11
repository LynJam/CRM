package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("user")
public class UserEntity {
    @TableId
    private String userId;
    private String username;
    private Boolean gender;
    private String phone;
    private String image;
    private String description;
    private String password;
}


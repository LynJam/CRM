package cn.edu.scnu.vo;

import lombok.Data;

@Data
public class CustomerVo {
    private String userId;
    private String username;
    private Boolean gender;
    private String phone;
    private String image;
    private String description;
    private Boolean upUser;

    private Boolean added;
}

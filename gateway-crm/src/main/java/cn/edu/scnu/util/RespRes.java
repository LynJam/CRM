package cn.edu.scnu.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 规定：
 * 10000    数据正常
 *
 * 20000    token 异常，需要刷新
 * 20001    登录过期
 */
@Data
@AllArgsConstructor
public class RespRes<T> {
    private T data;
    private int code;
    private String msg;

    public static <T> RespRes<T> success(T data) {
        return new RespRes<>(data, 10000, "success");
    }

    public static <T> RespRes<T> error(int code, String msg) {
        return new RespRes<>(null, code, msg);
    }

    public static <T> RespRes<T> errorOfToken() {
        return error(20000, "token 异常");
    }

}


package cn.edu.scnu.dto;

import cn.edu.scnu.enums.ErrorEnum;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 规定：
 * 0    数据正常
 *
 * 20000    token 异常，需要刷新
 * 20001    登录过期
 *
 * 50000    业务异常
 * 50010    参数校验异常
 * ...
 *
 * 11111    未知异常
 *
 */
@Data
@AllArgsConstructor
public class RespRes<T> {
    private T data;
    private int code;
    private String msg;

    public static <T> RespRes<T> successIfDataNotNull(T data, ErrorEnum errorType) {
        if(ObjectUtil.isNull(data)) {
            return error(errorType.getCode(), errorType.getMsg());
        }
        return success(data);
    }

    public static <T> RespRes<T> success(T data) {
        return new RespRes<>(data, 0, "success");
    }

    public static <T> RespRes<T> error(int code, String msg) {
        return new RespRes<>(null, code, msg);
    }

    public static <T> RespRes<T> error(ErrorEnum errorEnum) {
        return error(errorEnum.getCode(), errorEnum.getMsg());
    }

    public static <T> RespRes<T> errorOfToken() {
        return error(ErrorEnum.ERROR_OF_TOKEN);
    }

    public static <T> RespRes<T> errorOfLoginExpired() {
        return error(ErrorEnum.LOGIN_EXPIRED);
    }

}


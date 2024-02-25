package cn.edu.scnu.enums;

public enum ErrorEnum {
    //NO_ADMIN(10010, "非管理员"),
    NAME_OR_PWD_ERROR(20010, "账号或密码错误"),
    //USER_EXISTS(10010, "用户已存在"),
    //USER_NO_EXISTS(20010, "用户不存在"),
    //USER_NOT_LOGGED_IN(10100, "该用户未登录"),
    //TRANSACTION_ERROR(10030, "事务问题，导致回滚，请重试！"),
    ERROR_OF_TOKEN(20000, "token 异常"),
    LOGIN_EXPIRED(20001, "登录过期"),

    BUSH_EXCEPT(50000, "业务异常"),
    VALIDATE_EXCEPT(50010, "校验不通过"),

    //CANT_CHANG_ADMINISTER(10010, "不能把自己改成管理员"),
    //ERROR_OVER_DATABASE(10010, "操作数据库出错"),
    //
    //IN_SUFFICE_PERMITS(10010, "权限不足"),
    //
    //NO_DEL_PERMITS(10010, "权限不足，无法删除"),
    //NO_CHANGE_PERMITS(10010, "权限不足，无法修改"),
    //NO_ADD_PERMITS(10010, "权限不足，增加信息失败"),
    //CANNOT_FOUND(10020, "查不到该数据"),
    HAS_EXISTED(50030, "该条记录已存在"),

    UNKNOWN_EXCEPT(11111, "未知异常")
    ;
    private final int code;
    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}


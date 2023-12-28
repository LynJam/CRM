package cn.edu.scnu.exception;

import cn.edu.scnu.enums.ErrorEnum;

public class BizException extends RuntimeException{
    protected int status = 200;
    protected int errCode = ErrorEnum.BUSH_EXCEPT.getCode();

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, int errCode) {
        super(message);
        this.errCode = errCode;
    }

    public BizException(ErrorEnum error) {
        super(error.getMsg());
        this.errCode = error.getCode();
    }

    public int getErrCode() {
        return errCode;
    }
}

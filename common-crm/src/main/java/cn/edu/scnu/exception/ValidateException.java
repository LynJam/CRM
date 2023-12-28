package cn.edu.scnu.exception;

import cn.edu.scnu.enums.ErrorEnum;

public class ValidateException extends BizException {

    {
        errCode = ErrorEnum.VALIDATE_EXCEPT.getCode();
    }

    public ValidateException() {
        super(ErrorEnum.VALIDATE_EXCEPT);
    }

    public ValidateException(String message) {
        super(message);
    }
}

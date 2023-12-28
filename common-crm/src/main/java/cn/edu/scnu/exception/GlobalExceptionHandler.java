package cn.edu.scnu.exception;

import cn.edu.scnu.dto.RespRes;
import cn.edu.scnu.enums.ErrorEnum;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ValidateException.class)
    @ResponseBody
    public RespRes validateExceptionHandler(HttpServletRequest req, ValidateException e) {
        log.error("{} 原因是： ", e.getMessage(), e);
        return RespRes.error(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public RespRes bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("业务异常！原因是：", e);
        return RespRes.error(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RespRes exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是：", e);
        return RespRes.error(ErrorEnum.UNKNOWN_EXCEPT);
    }
}

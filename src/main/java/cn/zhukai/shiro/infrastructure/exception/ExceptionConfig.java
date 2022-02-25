package cn.zhukai.shiro.infrastructure.exception;

import cn.zhukai.shiro.entity.ResultBean;
import cn.zhukai.shiro.infrastructure.enums.TypeEnum;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:31
 **/

@ControllerAdvice
public class ExceptionConfig {

    @ResponseBody
    @ExceptionHandler(CuteException.class)
    public ResultBean cuteException(CuteException cute) {
        return new ResultBean(cute);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResultBean exception(Throwable e) {
        return new ResultBean(e);
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public ResultBean<TypeEnum> errorHandler(AuthorizationException auth) {
        return new ResultBean<>(TypeEnum.NO_AUTH);
    }
}

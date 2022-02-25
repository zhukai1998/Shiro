package cn.zhukai.shiro.infrastructure.exception;

import cn.zhukai.shiro.infrastructure.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:29
 **/

@AllArgsConstructor
@Getter
public class CuteException extends RuntimeException{
    private int code;
    private String message;

    public CuteException(TypeEnum typeEnum) {
        this.code = typeEnum.getCode();
        this.message = typeEnum.getMsg();
    }

    public CuteException(TypeEnum typeEnum, String msg) {
        this.code = typeEnum.getCode();
        this.message = msg;
    }

}

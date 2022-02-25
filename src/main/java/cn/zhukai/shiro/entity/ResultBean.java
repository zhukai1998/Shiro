package cn.zhukai.shiro.entity;

import cn.zhukai.shiro.infrastructure.enums.TypeEnum;
import cn.zhukai.shiro.infrastructure.exception.CuteException;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:32
 **/

@Getter
public class ResultBean<T> {
    private int code;
    private String msg;
    private T data;

    public ResultBean(T data) {
        this.code = TypeEnum.SUCCESS.getCode();
        this.msg = TypeEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public ResultBean(TypeEnum typeEnum) {
        this.code = typeEnum.getCode();
        this.msg = typeEnum.getMsg();
    }

    public ResultBean(Exception e) {
        this.code = TypeEnum.FAILED.getCode();
        this.msg = TypeEnum.FAILED.getMsg();
        this.data = (T)e.getMessage();
    }

    public ResultBean(CuteException e) {
        this.code = e.getCode();
        this.msg = e.getMessage();
        this.data = (T)e.getMessage();
    }

}

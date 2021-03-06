package cn.zhukai.shiro.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:28
 **/

@Getter
@AllArgsConstructor
public enum TypeEnum {
    SUCCESS(200, "操作成功"),
    FAILED(0, "操作失败"),
    PASSWORD_OR_ACCOUNT_ERROR(201, "密码或者账号错误！"),
    ACCOUNT_STOP_USING(202, "账号已停用，请联系管理员!"),
    ACCOUNT_UNUSUAL(203,"账户异常，请联系管理员！"),
    BAD_PARAM(204,"接口参数错误，请联系管理员！"),
    TOKEN_EXPIRE(205,"用户身份过期,请重新登录!"),
    TOKEN_NOT_IN_REQUEST_HEADER(206,"请求头中未携带token信息!"),
    TOKEN_EXCEPTION(207,"token无效或过期"),
    NO_AUTH(401,"用户无权限访问此接口"),
    ERROR_ACCOUNT(400,"用户名或密码错误"),
    SERVER_ERROR(500, "服务器异常"),
    COPY_ERROR(300,"拷贝对象属性出错" ),
    USER_NOT_EXISTS(301,"用户不存在" );

    private int code;
    private String msg;


}

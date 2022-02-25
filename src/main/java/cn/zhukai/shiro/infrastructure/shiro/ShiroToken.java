package cn.zhukai.shiro.infrastructure.shiro;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:48
 **/

@AllArgsConstructor
public class ShiroToken implements AuthenticationToken {

    private String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString() {
        return token;
    }
}

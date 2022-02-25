package cn.zhukai.shiro.infrastructure.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 这个类最主要的目的是：
 * 当请求需要校验权限，token是否具有权限时
 * 构造出主体 subject 执行 login
 * </p>
 *
 * @author zhukai
 * @date 2022/2/24 17:11
 **/


public class ShiroFilter extends BasicHttpAuthenticationFilter  {

    /**
     * 执行登录认证
     * 此方法在进行登录时，
     * 如果token存在，则进行提交登录
     * 如果token不存在，则已登录，则直接跳过该方法
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = ((HttpServletRequest) request).getHeader("token");
        if(token == null || token.equals("")){
            return false;
        }
        ShiroToken shiroToken = new ShiroToken(token);
        // 提交到 ModularRealmAuthenticator（当有多种校验方式时，可继承该类）决定哪个realm执行doAuthenticate操作
        getSubject(request, response).login(shiroToken);
        return true;
    }

}

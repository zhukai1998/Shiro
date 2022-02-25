package cn.zhukai.shiro.infrastructure.shiro;

import cn.zhukai.shiro.infrastructure.enums.TypeEnum;
import cn.zhukai.shiro.infrastructure.exception.CuteException;
import cn.zhukai.shiro.infrastructure.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/24 16:00
 **/


public class ShiroRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ShiroToken;
    }

    // filter --- doGetAuthenticationInfo --- doGetAuthorizationInfo
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // JwtRealm 身份验证开始执行
        String token = String.valueOf(authenticationToken.getCredentials());
        //校验token密文是否合法
        String id = JwtUtil.verify(token, "salt");
        if(id == null) {
            // 抛出异常
        }
        // TODO 连库校验ID合法性, 并获取用户对象


        // getName() 是指获取 realm 的 name
        // 第一个参数是doGetAuthorizationInfo的principalCollection
        return new SimpleAuthenticationInfo(token, token, getName());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token = principalCollection.toString();
        // JwtRealm 身份认证开始
        // 此处只是获得ID，盐值 = null
        String id = JwtUtil.verify(token, null);
        if(id == null) {
            // 抛出异常
        }
        // TODO 连库校验ID合法性, 并获取角色和权限
        // 仿写初始化开始
        // 保持与ShiroController里的一样
        Map<String, Map<String, String>> result = new ConcurrentHashMap<>();
        Map<String, String> admin = new HashMap<>();
        admin.put("account", "admin");
        admin.put("password", "zhukai");
        admin.put("id", "0");
        admin.put("role", "admin");
        admin.put("permission", "ALL");
        result.put("admin", admin);

        Map<String, String> zhukai = new HashMap<>();
        zhukai.put("role", "user");
        zhukai.put("permission", "1");
        zhukai.put("account", "12345678900");
        zhukai.put("password", "zhukai");
        zhukai.put("id", "1");
        result.put("12345678900", zhukai);

        Map<String, String> user = new HashMap<>();
        Iterator<Map.Entry<String, Map<String, String>>> entries  = result.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, Map<String, String>> entry = entries.next();
            Map<String, String> map = entry.getValue();
            if(map.get("id").equals(id)) {
                user = map;
                break;
            }
        }
        if(user.isEmpty()) {
            throw new CuteException(TypeEnum.USER_NOT_EXISTS);
        }
        // 仿写初始化结束

        // 获取所有的角色
        String[] roles = user.get("role").split(",");
        String[] permissions = user.get("permission").split(",");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        simpleAuthorizationInfo.addRoles(Arrays.asList(roles));
        // 添加权限
        simpleAuthorizationInfo.addStringPermissions(Arrays.asList(permissions));
        
        return simpleAuthorizationInfo;
    }


}

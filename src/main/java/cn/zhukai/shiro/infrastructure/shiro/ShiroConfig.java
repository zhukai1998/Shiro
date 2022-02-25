package cn.zhukai.shiro.infrastructure.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:27
 **/

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    /**
     * securityManager 是 Shiro 架构的核心
     * 通过它来链接Realm和用户（subject）
     * @param shiroRealm
     * @return
     */
    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 若有多个realm，这里添加继承后的 ModularRealmAuthenticator
//        defaultWebSecurityManager.setAuthenticator();
        // 设置 realm
        defaultWebSecurityManager.setRealm(shiroRealm);
        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }

    /**
     * shiro 内置过滤器，可以实现相关的拦截
     * 常用的过滤类型
     * anon: 无需认证
     * authc: 需要认证
     * user: 如果使用rememberMe的功能可以直接访问
     * perms: 该资源必须得到资源权限才可以访问
     * role: 该资源必须得到角色权限才可以访问
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 设置未登录跳转的路径
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        // 设置过滤路径相对应的类型
        Map<String, String> filterUrl = new LinkedHashMap<>(1);
        filterUrl.put("/shiro/login", "anon");
        filterUrl.put("/shiro/register", "anon");
        filterUrl.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterUrl);
        // 设置自定义过滤器
        Map<String, Filter> filter = new HashMap<>(1);
        filter.put("jwt", new ShiroFilter());
        shiroFilterFactoryBean.setFilters(filter);
        return shiroFilterFactoryBean;
    }

    /**
     * 以下Bean开启shiro权限注解
     *
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator creator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }



}

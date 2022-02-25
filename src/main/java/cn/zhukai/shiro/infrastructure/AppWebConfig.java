package cn.zhukai.shiro.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/24 23:15
 **/

@Configuration
public class AppWebConfig implements WebMvcConfigurer {


    /**
     * 解决跨域问题
     * 源（origin）就是协议、域名和端口号。
     * URL由协议、域名、端口和路径组成，如果两个URL的协议、域名和端口全部相同，
     * 则表示他们同源。否则，只要协议、域名、端口有任何一个不同，就是跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}

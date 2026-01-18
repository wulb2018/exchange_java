package com.wulb2018.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author wulubin
 * @date 2026/1/17
 * @description TODO
 */
@Configuration
public class GlobalCorsConfig { //implements WebFluxConfigurer

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 这里仅为了说明问题，配置为放行所有域名，生产环境请对此进行修改
        config.addAllowedOriginPattern("*");
        // 放行的请求头
        config.addAllowedHeader("*");
        // 放行的请求方式，主要有：GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedMethod("*");
        // 暴露头部信息
        config.addExposedHeader("*");
        // 是否发送cookie
        //config.setAllowCredentials(true);
        //config.addExposedHeader("Access-Control-Allow-Origin");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .maxAge(3600L);
//    }

//    @Bean
//    public CorsWebFilter corsWebFilter() {
////        CorsConfiguration config = new CorsConfiguration();
////
////        // ⚠ 不要用 allowedOrigins("*") + credentials
////        config.addAllowedOrigin("*"); // 1允许任何域名使用
////        config.addAllowedHeader("*"); // 2允许任何头
////        config.addAllowedMethod("*"); // 3允许任何方法（post、get等）
////        config.setAllowCredentials(true);
////        config.setMaxAge(3600L);
////
////        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.getCorsConfiguration("/**", config);
////        source.registerCorsConfiguration("/**", config);
////
////        return new CorsWebFilter(source);
//    }
}

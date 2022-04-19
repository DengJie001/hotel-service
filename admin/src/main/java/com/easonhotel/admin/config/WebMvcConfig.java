package com.easonhotel.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决前后端请求跨域
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * http请求方法集合
     */
    private static final String HTTP_REQUEST_METHOD_POST = "POST";

    private static final String HTTP_REQUEST_METHOD_GET = "GET";

    private static final String HTTP_REQUEST_METHOD_PUT = "PUT";

    private static final String HTTP_REQUEST_METHOD_OPTIONS = "OPTIONS";

    private static final String HTTP_REQUEST_METHOD_DELETE = "DELETE";

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods(
                        HTTP_REQUEST_METHOD_POST,
                        HTTP_REQUEST_METHOD_GET,
                        HTTP_REQUEST_METHOD_PUT,
                        HTTP_REQUEST_METHOD_OPTIONS,
                        HTTP_REQUEST_METHOD_DELETE
                )
                .maxAge(3600)
                .allowCredentials(true);
    }
}

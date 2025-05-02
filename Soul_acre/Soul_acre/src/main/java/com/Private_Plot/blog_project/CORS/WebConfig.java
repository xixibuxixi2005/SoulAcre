package com.Private_Plot.blog_project.CORS;

import com.Private_Plot.blog_project.CORS.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilterRegistrationBean() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenFilter());
        registrationBean.addUrlPatterns("/Home.html"); // 配置需要过滤的URL路径
        registrationBean.setName("tokenFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}


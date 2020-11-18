package com.red_projects.mediateca.configuration;

import com.red_projects.mediateca.security.filters.AdminJwtFilter;
import com.red_projects.mediateca.security.filters.UserJwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<UserJwtFilter> userFilter() {
        FilterRegistrationBean<UserJwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserJwtFilter());
        registrationBean.addUrlPatterns("/media-services/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminJwtFilter> adminFilter() {
        FilterRegistrationBean<AdminJwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminJwtFilter());
        registrationBean.addUrlPatterns("/admin-services/*");
        return registrationBean;
    }
}



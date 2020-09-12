package com.redprojects.mediateca;

import com.redprojects.mediateca.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfiguration {
    //@Bean
/*    public FilterRegistrationBean<JwtFilter> authFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/open-services/*");
        return registrationBean;
    }*/
}



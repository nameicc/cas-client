package com.tingyu.security.config;

import com.tingyu.security.properties.CASClientProperties;
import com.tingyu.security.properties.CASServerProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.annotation.Resource;

@Configuration
public class CASSecurityConfig {

    @Resource
    private CASServerProperties casServerProperties;

    @Resource
    private CASClientProperties casClientProperties;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * ServiceProperties中主要配置Client的登录地址，这个地址就是在CAS Server登录成功之后，重定向的地址
     **/
    @Bean
    ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casClientProperties.getLogin());
        return serviceProperties;
    }

    /**
     * CasAuthenticationEntryPoint是CAS验证的入口，这里首先设置CAS Server的登录地址，同时将前面的ServiceProperties
     * 设置进去，这样当它登录成功之后，就知道往哪里跳转了
     **/
    @Bean
    @Primary
    AuthenticationEntryPoint authenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casServerProperties.getLogin());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * TicketValidator配置Ticket校验地址，CAS Client拿到Ticket要去CAS Server上校验
     * 默认校验地址是：https://localhost:8443/cas/proxyValidate?ticket=xxx
     **/
    @Bean
    TicketValidator ticketValidator() {
        return new Cas20ProxyTicketValidator(casServerProperties.getPrefix());
    }

    /**
     * CasAuthenticationProvider主要用来处理验证逻辑
     **/
    @Bean
    CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(ticketValidator());
        casAuthenticationProvider.setUserDetailsService(userDetailsService);
        casAuthenticationProvider.setKey("nameicc");
        return casAuthenticationProvider;
    }

    /**
     * CasAuthenticationFilter是CAS认证的过滤器，过滤器将请求拦截下来之后，交由CasAuthenticationProvider
     * 来做具体处理
     **/
    @Bean
    CasAuthenticationFilter casAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setServiceProperties(serviceProperties());
        casAuthenticationFilter.setAuthenticationManager(new ProviderManager(authenticationProvider));
        return casAuthenticationFilter;
    }

    /**
     * SingleSignOutFilter表示接受CAS Server发出的注销请求，所有的注销请求都将从CAS Client转发到CAS Server，
     * CAS Server处理完成之后，会通知所有的CAS Client注销登录
     **/
    @Bean
    SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * LogoutFilter则是配置将注销请求转发到CAS Server
     **/
    @Bean
    LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casServerProperties.getLogout(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casClientProperties.getLogoutRelative());
        return logoutFilter;
    }

}

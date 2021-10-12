package com.tingyu.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cas.client")
@Component
@Data
public class CASClientProperties {

    private String prefix;

    private String login;

    private String logoutRelative;

    private String logout;

}

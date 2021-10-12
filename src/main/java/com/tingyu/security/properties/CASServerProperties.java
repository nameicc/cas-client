package com.tingyu.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cas.server")
@Component
@Data
public class CASServerProperties {

    private String prefix;

    private String login;

    private String logout;

}

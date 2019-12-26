package com.satyy.ldap.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Satyam Singh (satyamsingh00@gmail.com)
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ldap.user.search")
public class LdapUserProperties {
    private String base = "ou=people";
    private String filter = "uid={0}";
    private String passwordAttribute = "userPassword";
}

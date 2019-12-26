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
@ConfigurationProperties(prefix = "ldap.group.role")
public class LdapGroupProperties {
    private String attribute = "cn";
    private String prefix = "ROLE_";
    private String filter = "member={0}";
}

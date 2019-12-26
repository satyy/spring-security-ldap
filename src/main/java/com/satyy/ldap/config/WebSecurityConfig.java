package com.satyy.ldap.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

/**
 * @author Satyam Singh (satyamsingh00@gmail.com)
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.connection.url}")
    private String ldapConnectionUrl;

    @Value("${ldap.authorization.group.name}")
    private String authGroupName;

    @Autowired
    private LdapGroupProperties groupProperties;

    @Autowired
    private LdapUserProperties userProperties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .ldapAuthentication()
                .userSearchBase(userProperties.getBase())
                .userSearchFilter(userProperties.getFilter())
                .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
                .contextSource(contextSource())
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute(userProperties.getPasswordAttribute());
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {

        final DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource(),
            "ou=groups") {

            @Override
            public Set<GrantedAuthority> getGroupMembershipRoles(final String userDn,
                                                                 final String username) {
                final Set<GrantedAuthority> groupMembershipRoles =
                        super.getGroupMembershipRoles(userDn, username);
                boolean isValidMember = false;
                final String authorizeRole = groupProperties.getPrefix() + authGroupName;

                for (GrantedAuthority grantedAuthority : groupMembershipRoles) {
                    if (authorizeRole.equalsIgnoreCase(grantedAuthority.toString())) {
                        isValidMember = true;
                        break;
                    }
                }

                if (!isValidMember) {
                    throw new BadCredentialsException("User must be a member of group - " + authGroupName);
                }
                return groupMembershipRoles;
            }
        };
        authoritiesPopulator.setGroupRoleAttribute(groupProperties.getAttribute());
        authoritiesPopulator.setRolePrefix(groupProperties.getPrefix());
        authoritiesPopulator.setGroupSearchFilter(groupProperties.getFilter());

        return authoritiesPopulator;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(ldapConnectionUrl);
    }
}
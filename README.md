# spring-security-ldap
- Implementation of User Authentication and Authorization service using `spring-security-ldap`.
- User is Authorized by validating credential provivided by the user from the one present in LDAP server.
- Once, user is authenticated, user will be validated if the user is authorized to access the resource by validating if the user is part of   group.

## About Spring Security LDAP
`LDAP` stands for `Lightweight Directory Access Protocol`. It is often used by organizations as a central repository for user information and as an authentication service. It can also be used to store the role information and can by used as an authorization service.

LDAP authentication & authorization using `spring-security` involves following:
  1. Obtaining the unique LDAP “Distinguished Name”, or DN, from the login user-id. This involves performing a search in the directory.
     for e.g. `dn: uid=ssingh,ou=people,dc=satyam,dc=com`
  2. Authenticating the user, either by binding as that user or by performing a remote “compare” operation of the user's password against        the password attribute in the directory entry for the DN.
  3. Loading the list of authorities/groups for the authenticated user and validating if the user is authorized to access the resource if        the user is member of the desired group.
 
## About Application
Spring-Security intercept every request coming to the application and redirect it to spring-security's default login page. Once, the user   provides credentials and try to login, the user's credential will be authenticated against ldap server and on successful authentication, user will be able to access the resource if user is part of the group and authorized to access it.

- Instead of intercepting every request, spring-security support authenticating only request matching particular request or matching some     regex pattern.
  For e.g, to intercept any request with `\login` endpoint can be done by changing 
          **.anyRequest().fullyAuthenticated()** to **.antMatchers("/login").fullyAuthenticated()**
   in `configure(final HttpSecurity http)` funtion in configuration bean at `/src/main/java/com/satyy/ldap/config/WebSecurityConfig.java`
  
   

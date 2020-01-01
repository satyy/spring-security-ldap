# spring-security-ldap
- Implementation of User Authentication and Authorization service using `spring-security-ldap`.
- User is Authorized by validating credential provivided by the user from the one present in LDAP server.
- Once, user is authenticated, user will be validated if the user is authorized to access the resource by validating if the user is part of group.

## Pre-requisite:
1. Java - 11
2. Gradle

## About Spring Security LDAP
`LDAP` stands for `Lightweight Directory Access Protocol`. It is often used by organizations as a central repository for user information and as an authentication service. It can also be used to store the role information and can by used as an authorization service.

USER authentication & authorization using `spring-security-ldap` involves following:
  1. Obtaining the unique LDAP “Distinguished Name”, or DN, from the login user-id. This involves performing a search in the directory.
     for e.g. `dn: uid=ssingh,ou=people,dc=satyam,dc=com`
  2. Authenticating the user, either by binding as that user or by performing a remote “compare” operation of the user's password against        the password attribute in the directory entry for the DN.
  3. Loading the list of authorities/groups for the authenticated user and validating if the user is authorized to access the resource if        the user is member of the desired group.
 
## About Application
Spring-Security intercept every request coming to the application and redirect it to spring-security's default login page. Once, the user   provides credentials and try to login, the user's credential will be authenticated against ldap server and on successful authentication, user will be able to access the resource if user is part of the group and authorized to access it.

- Instead of intercepting every request, spring-security support authenticating only request matching particular request or matching some regex pattern.
  For e.g, to intercept any request with `\login` endpoint can be done by changing 
          **.anyRequest().fullyAuthenticated()** to **.antMatchers("/login").fullyAuthenticated()**
   in `configure(final HttpSecurity http)` funtion in configuration bean at `/src/main/java/com/satyy/ldap/config/WebSecurityConfig.java`
   
### Configuration
All the necessary configurations are externalized from the code and are part `application.properties`
<pre>
1.  <b>ldap.connection.url</b>                -   Ldap server connection url, having the root directory(for e.g: dc=satyam, dc=com)
2.  <b>ldap.authorization.group.name</b>      -   Group name user is supposed to be part of, to validate user is authorized or not.
3.  <b>ldap.user.search.base</b>              -   User search base-directory relative to root directory(which was part of connection url). Using these information spring-security forms the absolute directory where user information is present in ldap server. For e.g, in this case if <i>ldap.user.search.base</i> has configured value <i>ou=people</i> then, the absolute path becomes <i>ou=people, dc=satyam, dc=com</i>
4.  <b>ldap.user.search.filter</b>            -   Search filter applied on the distinguised name(dn) to search user inside ldap's user search base directory. For e.g, in the current application, this property is set to `uid={0}` which means value of `uid` in `dn` will be matched to the entered userId on the login page. By default, spring maps userId field in login page to {0} and password to {1}.
5.  <b>ldap.user.search.passwordAttribute</b> -   password attribute name in the `dn`. User entered password will be matched with this field in ldap user entry.
6.  <b>ldap.group.role.attribute</b>          -   attribute name which is mapped to group names in ldap server. Defualt value is `cn`
7.  <b>ldap.group.role.prefix</b>             -   prefix for each group name which spring add before every group the user is part of. Default value is `Role_`
8.  <b>ldap.group.role.filter</b>             -   Group filter, to identify if the user is part of the group or not. Default value is `member={0}` where, {0} is user id in the login page.
9.  <b>spring.ldap.embedded.ldif</b>          -   ldif file, containing entries to be loaded to in memory ldap server for this sample application. There is sample `test-server.ldif` file at resource which has data in ldif format and which creates dummy organisation hirarchy and add few user under it and also create two groups and assign user in that groups.
10. <b>spring.ldap.embedded.base-dn</b>       -   root dn for the ldap server.
11. <b>spring.ldap.embedded.port</b>          -   ldap server port
12. <b>logging.level.root</b>                 -   application log level
13. <b>server.port</b>                        -   port on which application will run. Currently, its configured to port `8888`.
</pre>  

## Build and Run
1. Checkout repo.
2. run cmd `sh run-app.sh`

### Port Used 
The appication is configured to run on port **8888** which can be changed by modifying **server.port** in application.properties 

## Verify spring-security-ldap
1. Go to browser and hit the Get endpoint <i><b>/home</b></i> exposed by the application,
```
          http://127.0.0.1:8888/home
```
   which will redirect to the login page.
   
2. The app will verify the credentials against the one configured in **test-server.ldif** file and on successful authentication, the app will check if the user is part of group **test_dev_1** which is configured as property **ldap.authorization.group.name** in <i>application.properties</i>.
3. Only, if the user is part of the group, the user will be able to access the resource. Otherwise, relavant error message will be shown at the <i>login page</i>.

## Implementation Class File

**WebSecurityConfig.java** at `src/main/java/com/satyy/ldap/config`

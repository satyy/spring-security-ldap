# spring-security-ldap
- Implementation of User Authentication and Authorization service using spring-security-ldap.
- User is Authorized by validating credential provivided by the user from the one present in LDAP server.
- Once, user is authenticated, user will be validated if the user is authorized to access the resource by validating if the user is part of   group.

## About Spring Security LDAP
`LDAP` stands for `Lightweight Directory Access Protocol`. It is often used by organizations as a central repository for user information and as an authentication service. It can also be used to store the role information and can by used as an authorization service.

LDAP authentication & authorization using `spring-security` involves following:
  1. Obtaining the unique LDAP “Distinguished Name”, or DN, from the login user-id. This involves performing a search in the directory.
     for e.g. `dn: uid=ssingh,ou=people,dc=satyam,dc=com`
  2. Authenticating the user, either by binding as that user or by performing a remote “compare” operation of the user's password against        the password attribute in the directory entry for the DN.
  3. Loading the list of authorities/groups for the authenticated user and validating if the user is authorized to access the resource if        the user is member of the desired group.

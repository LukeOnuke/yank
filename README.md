# yank
Secure and low resource overhang server management

***Currently in really early development***

## Installation
1. Download the jar from github actions or build it yourself
2. Add a `program.properties` file containing this config in 
   the folder where the jar is located.
   
    
`program.properties`
```properties
spring.security.oauth2.client.registration.google.client-id=<google-oauth2-client-id>
spring.security.oauth2.client.registration.google.client-secret=<google-oauth2-client-secret>
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:h2:file:<full path to db file (see example)>
```
For example /home/users/mcserver/yank/db will use the `yank` folder 
in `/home/users/mcserver` and create the db files starting with the db prefix.

⚠⚠⚠ It has to be a non relative filepath ⚠⚠⚠

---
3. Add the users.conf file in the folder where the jar is located. The `users.conf` file
    contains the email addresses of the users that have access to the webinterface, they
    are separated by newlines. 
    For example:

`users.conf`

```text
johndoe@gmail.com
nn.pearson@gmail.com
superuser@gmail.com
```
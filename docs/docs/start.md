# Start
1. Download the jar from releases
2. Execute the jar for it to finish first time setup, then edit the `program.properties` file

`program.properties`
```properties
spring.security.oauth2.client.registration.google.client-id=<google-oauth2-client-id>
spring.security.oauth2.client.registration.google.client-secret=<google-oauth2-client-secret>
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:h2:file:<full path to db file (see example)>
minecraft.server.ip=<server-ip>
minecraft.server.port=<server-port>
minecraft.server.start=java -Xmx5120M -Xms1024M -jar server.jar -nogui
```
For example `/home/users/mcserver/yank/db` will use the `yank` folder
in `/home/users/mcserver` and create the db files starting with the db prefix.

> ⚠⚠⚠ It has to be an absolute filepath ⚠⚠⚠

---
3. Create google oauth keys

Follow the guide from https://developers.google.com/identity/protocols/oauth2/openid-connect#appsetup
***only before the authentificating the user title***

![oauth setup](https://i.imgur.com/PkfRQT7.png)



---
4. Add the users.conf file in the folder where the jar is located. The `users.conf` file
   contains the email addresses of the users that have access to the webinterface, they
   are separated by newlines.
   For example:

`users.conf`

```text
johndoe@gmail.com
nn.pearson@gmail.com
superuser@gmail.com
```
---
5. Get the minecraft server of your choice, rename it to `server.jar` and
   put it in the directory `<dir-where-yank-is-located>/server`
   
---
Finally you are done, start yank again and if you've done everything 
right it should start, navigate to http://localhost:8080 or 
http://yourdomain.example.com:8080 and it should work.

## Still need help?
Luckily we have a discord server!
https://discord.gg/VVuQB868wU
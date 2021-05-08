# Start
## Step 1, downloading yank
Download the jar from [releases](https://github.com/LukeOnuke/yank/releases/latest)
## Step 2
Execute the jar for it to finish first time setup, then edit the `program.properties` file .
The program has a selftest feature and will exit with code `-1`, that is normal for this 
stage of setting it up.

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
## Step 3, create google oauth keys

###Setting up OAuth 2.0
> ℹ ***this part of the guide, detailing oauth setup has been taken from
[Google Identity, oauth 2 setup](https://developers.google.com/identity/protocols/oauth2/openid-connect#appsetup)
published by [google identity](https://developers.google.com/identity)***

#### Obtain OAuth 2.0 credentials
You need OAuth 2.0 credentials, including a client ID and client secret, to authenticate users and gain access to Google's APIs.

To view the client ID and client secret for a given OAuth 2.0 credential, click the following text: Select credential. In the window that opens, choose your project and the credential you want, then click View.

Or, view your client ID and client secret from the Credentials page in API Console:

Go to the Credentials page.
Click the name of your credential or the pencil (create) icon. Your client ID and secret are at the top of the page.
Set a redirect URI
The redirect URI that you set in the API Console determines where Google sends responses to your authentication requests.
> ## Important for setup
> ***You need to set the redirect URI to `<yourdomain>:8080`***
> 
> ***Also here you check your client id and client secret and 
> input them to the properties file that has been generated after 
> running the application for the first time***
> ```properties
> spring.security.oauth2.client.registration.google.client-id=<client id>
> spring.security.oauth2.client.registration.google.client-secret=<client secret>
> ```
> Example image (yourdomain.example.com is a placeholder domain): 
> ![example image](https://i.imgur.com/PkfRQT7.png)

To create, view, or edit the redirect URIs for a given OAuth 2.0 credential, do the following:

Go to the Credentials page.
In the OAuth 2.0 client IDs section of the page, click a credential.
View or edit the redirect URIs.
If there is no OAuth 2.0 client IDs section on the Credentials page, then your project has no OAuth credentials. To create one, click Create credentials.

#### Customize the user consent screen
***If google doesn't prompt you to do this you can skip it, but it's nice to do.***

For your users, the OAuth 2.0 authentication experience includes a consent screen that describes the information that the user is releasing and the terms that apply. For example, when the user logs in, they might be asked to give your app access to their email address and basic account information. You request access to this information using the scope parameter, which your app includes in its authentication request. You can also use scopes to request access to other Google APIs.

The user consent screen also presents branding information such as your product name, logo, and a homepage URL. You control the branding information in the API Console.

To enable your project's consent screen:

Open the Consent Screen page in the Google API Console.
If prompted, select a project, or create a new one.
Fill out the form and click Save.
The following consent dialog shows what a user would see when a combination of OAuth 2.0 and Google Drive scopes are present in the request. (This generic dialog was generated using the Google OAuth 2.0 Playground, so it does not include branding information that would be set in the API Console.)



---
## Step 4, user.conf setup
   Add the users.conf file in the folder where the jar is located. The `users.conf` file
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
## Step 5, minecraft server setup
   Get the minecraft server of your choice, rename it to `server.jar` and
   put it in the directory `<dir-where-yank-is-located>/server`
   
---
Finally you are done, start yank again and if you've done everything 
right it should start, navigate to http://localhost:8080 or 
http://yourdomain.example.com:8080 and it should work.

## Still need help?
Luckily we have a discord server!
https://discord.gg/VVuQB868wU
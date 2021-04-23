# User endpoint (/api/v1/user/**)
This endpoint provides info about the user
## Me (/api/v1/user/me)
Will return info about the logged in user.

`GET` `(/api/v1/user/me)`

Will return

```json
{
  "picture": "link/to/picture",
  "id": 15531515,
  "first_name": "John",
  "family_name": "Doe",
  "email": "johndoe@gmail.com"
}
```

## Authorised (/api/v1/user/authorised)

*PUBLIC ENDPOINT*

This endpoint is used to indicate to the user if he is authorised, ofc the 
requests get blocked at the server level.

`GET` `(/api/v1/user/authorised)`

Will return
```json
{"authorised": false}
```
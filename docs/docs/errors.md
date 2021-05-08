#Errors
This page provides insight into the various error prompts 
that you might come across, this page is mostly directly
linked to on the error prompt, so no need to memorise 
what's what.

# Connection to the server can not be established.
The client couldn't connect to the server. 

This could be caused by : 
- The client's internet not functioning.
- The server being down.

    If that is the case, contact the administrator. If you are the administrator,
    restart the yank process. If that doesn't work, the logs should provide enough
    info.

# ServerFilesNotLockedTest
If this test failed that means that the files are locked , or
they are not present.

# ProgramPropertiesTest
This test fails when it can't write to the properties on first
startup.

# UserConfTest
This test fails when it can't write to user.conf on first
startup.
package com.lukeonuke.yank.endpoints;

import org.apache.juli.logging.LogFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RequestErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            String messageString = "Message not defined";
            if(((String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).startsWith("/api")){
                return new ResponseEntity<String>(HttpStatus.resolve(statusCode)); //Whatever i do it complains about something lol
            }
            if(message != null){
                messageString = message.toString();
            }
            return new ResponseEntity<String>(formatErrorPage("Status code " + statusCode, messageString), HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }


    private String formatErrorPage(String errorCode, String errorMessage){
        return "<!DOCTYPE html><html lang=\"en\"><head> <meta charset=\"UTF-8\"> <title>Yank - error</title> <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0\" crossorigin=\"anonymous\"></script> <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\"> <link href=\"/css/bootstrap.min.css\" rel=\"stylesheet\"></head><body> <div class=\"container\"> <h1 class=\"display-4\">{{errormsg}}</h1> <p class=\"lead\">{{errorcode}}</p><hr class=\"my-4\"> <p>If you think its a bug report it <a href=\"https://github.com/LukeOnuke/yank/issues/new\">here</a></p><p class=\"lead\"> <a class=\"btn btn-secondary btn-lg\" href=\"/\" role=\"button\">Go to home</a> <a class=\"btn btn-primary btn-lg\" href=\"/\" role=\"button\">Logout</a> </p></div></body></html>".replace("{{errormsg}}", errorMessage).replace("{{errorcode}}", errorCode);
    }
}

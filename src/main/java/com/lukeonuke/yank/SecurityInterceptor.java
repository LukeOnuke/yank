package com.lukeonuke.yank;

import com.lukeonuke.yank.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityInterceptor implements HandlerInterceptor {
    private ArrayList<String> userArr;

    private Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private void setup(){
        File file = new File("users.conf");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            userArr = new ArrayList<>(Files.readAllLines(Paths.get("users.conf")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(userArr == null){
            setup();
        }

        /*TODO: Add auth logic for google accounts*/
        logger.info(request.toString());
        logger.info(request.getServletPath());
        ArrayList<String> allowedUrls = new ArrayList<>(Arrays.asList("/index.html", "/", "css/bootstrap.min.css"));
        if (!allowedUrls.contains(request.getServletPath())) {
            OAuth2User user = ((OAuth2AuthenticationToken) request.getUserPrincipal()).getPrincipal();
            logger.info(user.getAttribute("email"));
            logger.info(userArr.toString());
            if (!userArr.contains(user.getAttribute("email"))) {
                throw new ForbiddenException();
            }
        }
        /*TODO: Read from arguments the server start command*/
        return true;
    }
}

package com.lukeonuke.yank.config;

import com.lukeonuke.yank.YankUtil;
import com.lukeonuke.yank.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class SecurityInterceptor implements HandlerInterceptor {
    private ArrayList<String> userArr;
    private final ArrayList<String> allowedUrls = new ArrayList<>(Arrays.asList("/index.html", "/", "css/bootstrap.min.css"));

    private Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private void setup(){
        try {
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


        if (!allowedUrls.contains(request.getServletPath())) {
            YankUtil.validateNotNull(request.getUserPrincipal()); //validate userPrincipal before casting

            OAuth2AuthenticationToken requestPrincipal = (OAuth2AuthenticationToken)request.getUserPrincipal();
            OAuth2User user = requestPrincipal.getPrincipal();

            //Check for not null
            YankUtil.validateNotNull(user);
            YankUtil.validateNotNull(user.getAttribute("email"));

            if (!userArr.contains(user.getAttribute("email"))) {
                throw new ForbiddenException();
            }
        }
        return true;
    }
}

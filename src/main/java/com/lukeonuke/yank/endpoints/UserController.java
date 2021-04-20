package com.lukeonuke.yank.endpoints;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
    private ArrayList<String> authenticatedUsers = null;

    @GetMapping("me")
    public HashMap<String, String> getUser(@AuthenticationPrincipal OAuth2User user){
        HashMap<String, String> data = new HashMap<>();
        data.put("picture", user.getAttribute("picture"));
        data.put("id", user.getName());
        data.put("first_name", user.getAttribute("given_name"));
        data.put("family_name", user.getAttribute("family_name"));
        data.put("email", user.getAttribute("email"));

        return data;
    }

    @GetMapping("authorised")
    public HashMap<String, Boolean> getAuthSimple(@AuthenticationPrincipal OAuth2User user){
        HashMap<String, Boolean> data = new HashMap<>();
        if(user == null){
            data.put("authorised", false);
            return data;
        }else{
            if(authenticatedUsers == null){
                try {
                    authenticatedUsers = SecurityInterceptor.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(authenticatedUsers.contains(user.getAttribute("email"))){
                data.put("authorised", true);
                return data;
            }
            data.put("authorised", false);
        }

        return data;
    }
}

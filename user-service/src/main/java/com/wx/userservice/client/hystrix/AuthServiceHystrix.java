package com.wx.userservice.client.hystrix;


import com.wx.userservice.client.AuthServiceClient;
import com.wx.userservice.domain.JWT;
import org.springframework.stereotype.Component;


@Component
public class AuthServiceHystrix implements AuthServiceClient {
    @Override
    public JWT getToken(String authorization, String type, String username, String password) {
        return null;
    }
}

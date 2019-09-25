package com.wx.userservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/currentUser")
public class WebController {

    @RequestMapping(method = RequestMethod.GET)
    public String getFoo() {
        return "i'm foo, " + UUID.randomUUID().toString();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }
}

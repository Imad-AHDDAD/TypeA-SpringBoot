package com.ahddad.typeaapp.controllers;

import com.ahddad.typeaapp.models.CustomUserDetails;
import com.ahddad.typeaapp.models.User;
import com.ahddad.typeaapp.services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    // Registration method
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public User processRegister(@RequestBody User user, HttpServletRequest request)
            throws Exception {
        return userService.registerUser(user, getSiteURL(request));
    }

    // getSiteUrl method
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public Void verifyUser(@Param("code") String code , HttpServletResponse httpServletResponse) {
        if (userService.verify(code)) {
            String projectUrl = "http://localhost:4200/successRegister";
            httpServletResponse.setHeader("Location", projectUrl);
            httpServletResponse.setStatus(302);
        } else {
            String projectUrl = "http://localhost:4200/register";
            httpServletResponse.setHeader("Location", projectUrl);
            httpServletResponse.setStatus(302);
        }
        return null;
    }

    // Login method
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) throws Exception {
        return userService.loginUser(user);
    }

    // la methode qui ajoute un element dans la table customUserDetails
    @PostMapping("/completeInfos/{token}")
    public CustomUserDetails completeInfos(@RequestBody CustomUserDetails details , @PathVariable String token) throws Exception {
        return userService.completeInfos(details , token);
    }

    // la methode qui retourne l'username a partir de token
    @GetMapping("/extractClaim/{token}")
    public Claims extractAllClaims(@PathVariable String token) {
        return userService.extractAllClaims(token);
    }

    @GetMapping("/a_rempli/{token}")
    public ResponseEntity<?> a_remplit(@PathVariable String token){
        return userService.a_rempli_les_donnees_pro(token);
    }

}

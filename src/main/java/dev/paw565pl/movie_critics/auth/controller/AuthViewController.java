package dev.paw565pl.movie_critics.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

    @GetMapping("/login")
    public String getLoginView() {
        return "redirect:/oauth2/authorization/keycloak";
    }
}

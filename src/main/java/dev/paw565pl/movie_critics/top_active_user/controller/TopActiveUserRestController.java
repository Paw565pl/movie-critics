package dev.paw565pl.movie_critics.top_active_user.controller;

import dev.paw565pl.movie_critics.top_active_user.response.TopActiveUserResponse;
import dev.paw565pl.movie_critics.top_active_user.service.TopActiveUserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/top-active-users")
public class TopActiveUserRestController {

    private final TopActiveUserService topActiveUserService;

    public TopActiveUserRestController(TopActiveUserService topActiveUserService) {
        this.topActiveUserService = topActiveUserService;
    }

    @GetMapping
    public Page<TopActiveUserResponse> findTop10ActiveUsers() {
        return topActiveUserService.findTop10ActiveUsers();
    }
}

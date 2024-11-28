package dev.paw565pl.movie_critics.top_active_user.service;

import dev.paw565pl.movie_critics.top_active_user.mapper.TopActiveUserMapper;
import dev.paw565pl.movie_critics.top_active_user.response.TopActiveUserResponse;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopActiveUserService {

    private final UserRepository userRepository;
    private final TopActiveUserMapper topActiveUserMapper;

    public TopActiveUserService(UserRepository userRepository, TopActiveUserMapper topActiveUserMapper) {
        this.userRepository = userRepository;
        this.topActiveUserMapper = topActiveUserMapper;
    }

    public Page<TopActiveUserResponse> findTop10ActiveUsers() {
        return userRepository
                .findAllOrderByRatingsAndCommentsCount(Pageable.ofSize(10))
                .map(topActiveUserMapper::toResponse);
    }
}

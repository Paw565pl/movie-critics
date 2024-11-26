package dev.paw565pl.movie_critics.comment.service;

import static dev.paw565pl.movie_critics.auth.utils.AuthUtils.hasRole;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.auth.role.Role;
import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.mapper.CommentMapper;
import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.comment.repository.CommentRepository;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.exception.MovieNotFoundException;
import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(
            MovieRepository movieRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            CommentMapper commentMapper) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    private CommentEntity findComment(Long id, Long movieId) {
        return commentRepository
                .findByIdAndMovieId(id, movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Page<CommentResponse> findAll(Long movieId, Pageable pageable) {
        return commentRepository.findAllByMovieId(movieId, pageable).map(commentMapper::toResponse);
    }

    public CommentResponse findByIdAndMovieId(Long id, Long movieId) {
        var comment = findComment(id, movieId);
        return commentMapper.toResponse(comment);
    }

    @Transactional
    public CommentResponse create(Long movieId, Jwt jwt, CommentDto dto) {
        var movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        var user = userRepository.findById(UserDetailsImpl.fromJwt(jwt).getId()).orElseThrow();

        var comment = commentMapper.toEntity(dto, user, movie);
        comment.setId(null);

        try {
            var savedComment = commentRepository.saveAndFlush(comment);
            return commentMapper.toResponse(savedComment);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "You have already created a comment for this movie.");
        }
    }

    @Transactional
    public CommentResponse update(Long id, Long movieId, Jwt jwt, CommentDto dto) {
        var comment = findComment(id, movieId);
        var user = UserDetailsImpl.fromJwt(jwt);

        var isAuthor = comment.getAuthor().getId().equals(user.getId());
        if (!isAuthor) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not allowed to update this comment.");
        }

        comment.setText(dto.text());

        var savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Transactional
    public void delete(Long id, Long movieId, Jwt jwt) {
        var comment = findComment(id, movieId);
        var user = UserDetailsImpl.fromJwt(jwt);

        var isAuthor = comment.getAuthor().getId().equals(user.getId());
        var isAdmin = hasRole(user.getAuthorities(), Role.ADMIN);
        if (!(isAuthor || isAdmin)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not allowed to delete this comment.");
        }

        commentRepository.deleteById(comment.getId());
    }
}

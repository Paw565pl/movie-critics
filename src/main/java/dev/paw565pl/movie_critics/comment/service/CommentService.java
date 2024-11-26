package dev.paw565pl.movie_critics.comment.service;

import dev.paw565pl.movie_critics.auth.details.UserDetailsImpl;
import dev.paw565pl.movie_critics.auth.role.Role;
import dev.paw565pl.movie_critics.comment.dto.CommentDto;
import dev.paw565pl.movie_critics.comment.mapper.CommentMapper;
import dev.paw565pl.movie_critics.comment.model.CommentEntity;
import dev.paw565pl.movie_critics.comment.repository.CommentRepository;
import dev.paw565pl.movie_critics.comment.response.CommentResponse;
import dev.paw565pl.movie_critics.movie.service.MovieService;
import dev.paw565pl.movie_critics.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static dev.paw565pl.movie_critics.auth.utils.AuthUtils.hasRole;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MovieService movieService;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, MovieService movieService, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.movieService = movieService;
        this.userRepository = userRepository;
    }

    public CommentEntity findEntity(Long id, Long movieId) {
        return commentRepository
                .findByIdAndMovieId(id, movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with given id does not exist."));
    }

    public Page<CommentResponse> findAllByMovieId(Long movieId, Pageable pageable) {
        movieService.findEntity(movieId);
        return commentRepository.findAllByMovieId(movieId, pageable).map(commentMapper::toResponse);
    }

    public CommentResponse findByIdAndMovieId(Long id, Long movieId) {
        var commentEntity = findEntity(id, movieId);
        return commentMapper.toResponse(commentEntity);
    }

    @Transactional
    public CommentResponse create(Long movieId, Jwt jwt, CommentDto dto) {
        var movieEntity = movieService.findEntity(movieId);
        var userEntity = userRepository.findById(UserDetailsImpl.fromJwt(jwt).getId()).orElseThrow();

        var commentEntity = commentMapper.toEntity(dto);
        commentEntity.setAuthor(userEntity);
        commentEntity.setMovie(movieEntity);

        try {
            var savedCommentEntity = commentRepository.saveAndFlush(commentEntity);
            return commentMapper.toResponse(savedCommentEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "You have already created a comment for this movie.");
        }
    }

    @Transactional
    public CommentResponse update(Long id, Long movieId, Jwt jwt, CommentDto dto) {
        var commentEntity = findEntity(id, movieId);
        var user = UserDetailsImpl.fromJwt(jwt);

        var isAuthor = commentEntity.getAuthor().getId().equals(user.getId());
        if (!isAuthor) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not allowed to update this comment.");
        }

        commentEntity.setText(dto.text());
        var savedCommentEntity = commentRepository.save(commentEntity);

        return commentMapper.toResponse(savedCommentEntity);
    }

    @Transactional
    public void delete(Long id, Long movieId, Jwt jwt) {
        var commentEntity = findEntity(id, movieId);
        var user = UserDetailsImpl.fromJwt(jwt);

        var isAuthor = commentEntity.getAuthor().getId().equals(user.getId());
        var isAdmin = hasRole(user.getAuthorities(), Role.ADMIN);
        if (!(isAuthor || isAdmin)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not allowed to delete this comment.");
        }

        commentRepository.deleteById(commentEntity.getId());
    }
}

package dev.paw565pl.movie_critics.admin.controller;

import dev.paw565pl.movie_critics.admin.service.AdminService;
import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import dev.paw565pl.movie_critics.user.response.UserResponse;
import dev.paw565pl.movie_critics.user.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminRestController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @IsAdmin
    @GetMapping("/export-movies")
    public ResponseEntity<ByteArrayResource> exportMovies() {
        var file = adminService.exportMoviesToJson();
        var formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        var fileName = "movies_" + formattedDateTime + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_JSON)
                .body(file);
    }

    @IsAdmin
    @PostMapping("/import-movies")
    public Map<String, String> importMovies(@RequestParam("file") MultipartFile file) {
        adminService.importMoviesFromJson(file);
        return Map.of("message", "Movies imported successfully.");
    }

    @IsAdmin
    @GetMapping("/users")
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @IsAdmin
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @IsAdmin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable UUID id) {
        userService.deleteById(id);
    }
}

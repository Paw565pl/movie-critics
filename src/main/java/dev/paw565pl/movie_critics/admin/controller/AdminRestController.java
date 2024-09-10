package dev.paw565pl.movie_critics.admin.controller;

import dev.paw565pl.movie_critics.admin.service.AdminService;
import dev.paw565pl.movie_critics.auth.annotation.IsAdmin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @IsAdmin
    @GetMapping("/export-movies")
    public ResponseEntity<?> exportMovies() {
        var file = adminService.createMoviesExportBytes();
        var formattedDateTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        var fileName = "movies_" + formattedDateTime + ".json";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_JSON)
                .body(file);
    }

    @IsAdmin
    @PostMapping("/import-movies")
    public Map<String, String> importMovies(@RequestParam("file") MultipartFile file) {
        adminService.importMovies(file);
        return Map.of("message", "Movies imported successfully.");
    }
}

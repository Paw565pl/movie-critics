package dev.paw565pl.movie_critics.image.watcher;

import dev.paw565pl.movie_critics.movie.repository.MovieRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageWatcher {

    @Value("${spring.servlet.multipart.location}")
    private String imagesDirectory;

    private final MovieRepository movieRepository;

    public ImageWatcher(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void deleteUnusedImages() {
        var posterImages = movieRepository.findDistinctPosterUrls().stream()
                .map(posterUrl -> posterUrl.substring(posterUrl.lastIndexOf("/") + 1))
                .collect(Collectors.toSet());
        var images = Arrays.stream(Paths.get(imagesDirectory).toFile().listFiles())
                .map(File::getName)
                .collect(Collectors.toSet());

        // Remove all used poster images from the set of images
        images.removeAll(posterImages);

        for (var image : images) {
            try {
                Files.deleteIfExists(Paths.get(imagesDirectory).resolve(image));
            } catch (IOException e) {
                log.error("Unable to delete file: {}", image);
            }
        }
    }

    @Async
    @Scheduled(cron = "0 0 3 * * *")
    public void asyncDeleteUnusedImages() {
        log.info("Deleting unused images...");
        deleteUnusedImages();
        log.info("Unused images deleted.");
    }
}

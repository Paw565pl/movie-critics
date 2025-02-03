package dev.paw565pl.movie_critics.image.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImageService {

    private static final String SAVE_FORMAT = "png";

    @Value("${spring.servlet.multipart.location}")
    private String imagesDirectory;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(imagesDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Images directory could not be created!");
        }
    }

    public UrlResource getFile(String fileName) {
        var file = Paths.get(imagesDirectory).resolve(fileName).normalize();

        try {
            var resource = new UrlResource(file.toUri());

            var exists = resource.exists() || resource.isReadable();
            if (!exists) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
        }
    }

    public String saveFile(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty.");
        }

        if (!isImageFile(imageFile)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid file type! Only JPG and PNG files are allowed.");
        }

        var originalFileName = imageFile.getOriginalFilename();
        var fileName = StringUtils.cleanPath(
                originalFileName.substring(0, originalFileName.lastIndexOf(".")).replaceAll("\\s+", "_"));
        var uniqueFileName = UUID.randomUUID() + "_" + fileName;
        var targetLocation = Paths.get(imagesDirectory).resolve(uniqueFileName);

        try {
            saveImage(imageFile.getInputStream(), 300, 460, targetLocation.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return createImageUrl(uniqueFileName);
    }

    private boolean isImageFile(MultipartFile file) {
        var allowedImageTypes = Set.of("image/jpeg", "image/png");
        return allowedImageTypes.contains(file.getContentType());
    }

    private void saveImage(InputStream imageStream, int width, int height, String targetLocation) throws IOException {
        Thumbnails.of(imageStream)
                .size(width, height)
                .outputFormat(SAVE_FORMAT)
                .outputQuality(0.9)
                .toFile(targetLocation);
    }

    private String createImageUrl(String fileName) {
        return String.format("/%s/%s.%s", imagesDirectory, fileName, SAVE_FORMAT);
    }
}

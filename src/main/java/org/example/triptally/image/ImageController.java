package org.example.triptally.image;

import org.example.triptally.auth.AuthValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/v1/images")
@PreAuthorize("hasRole('USER')")
public class ImageController {

    private final ImageService imageService;
    private final AuthValidationService authValidationService;

    public ImageController(ImageService imageService, AuthValidationService authValidationService) {
        this.imageService = imageService;
        this.authValidationService = authValidationService;
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long userId,
                                              @RequestParam("file") MultipartFile file,
                                              Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        imageService.uploadUserImage(userId, file);
        return ResponseEntity.ok("Image successfully uploaded");
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) {
        byte[] data = imageService.getUserImageData(imageId);
        String fileName = imageService.getUserImageName(imageId);
        String fileType = imageService.getUserImageType(imageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .contentType(MediaType.parseMediaType(fileType))
                .body(data);
    }

    @DeleteMapping("/delete-image/{userId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long userId, Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);
        imageService.deleteUserImage(userId);
        return ResponseEntity.ok("Image successfully deleted");

    }


}

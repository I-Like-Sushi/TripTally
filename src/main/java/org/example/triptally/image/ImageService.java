package org.example.triptally.image;

import jakarta.transaction.Transactional;
import org.example.triptally.exception.image.ImageNotFound;
import org.example.triptally.user.User;
import org.example.triptally.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(UserRepository userRepository,
                        ImageRepository imageRepository,
                        ImageMapper imageMapper) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public void uploadUserImage(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ImageNotFound("User not found"));

        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setFile(file);

        Image image = imageMapper.toEntity(imageRequest, user);

        imageRepository.save(image);
    }


    public byte[] getUserImageData(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow()
                .getImageData();
    }

    public String getUserImageName(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFound("User not found"))
                .getImageName();
    }

    public String getUserImageType(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFound("User not found"))
                .getImageType();
    }

    @Transactional
    public void deleteUserImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFound("Image not found"));

        image.setImageName(null);
        image.setImageType(null);
        image.setImageData(null);

        imageRepository.save(image);
        imageRepository.deleteById(imageId);
    }

}

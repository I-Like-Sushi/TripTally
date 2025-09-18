package org.example.eindopdrachtbackend.image;

import org.example.eindopdrachtbackend.user.User;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class ImageMapper {

    public Image toEntity(ImageRequest request, User user) {
        if (request == null || request.getFile() == null) {
            return null;
        }

        Image image = new Image();
        image.setUser(user);
        image.setImageName(request.getFile().getOriginalFilename());
        image.setImageType(request.getFile().getContentType());

        try {
            image.setImageData(request.getFile().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image bytes", e);
        }

        return image;
    }

    public ImageResponse toResponse(Image image) {
        if (image == null) {
            return null;
        }

        ImageResponse response = new ImageResponse();
        response.setImageName(image.getImageName());
        response.setImageType(image.getImageType());
        response.setImageData(image.getImageData()); // raw bytes

        return response;
    }

}

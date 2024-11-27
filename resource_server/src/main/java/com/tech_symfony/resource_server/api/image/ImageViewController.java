package com.tech_symfony.resource_server.api.image;

import com.tech_symfony.resource_server.system.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequiredArgsConstructor
public class ImageViewController {

    private final ImageService imageService;

    @GetMapping(value = "/image/{id}", produces = "image/png")
    @ResponseBody
    public FileSystemResource getImage(@PathVariable String id) throws Exception {
        return new FileSystemResource(imageService.getImage(id));
    }

    @PostMapping(value = "/image/upload", produces = "text/html")
    @ResponseBody
    public String sendImage(@RequestParam("image")MultipartFile image) throws Exception {
        return imageService.sendImage(image);
    }
}

package com.task.archivalLibrary.controller;

import com.task.archivalLibrary.entity.Document;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/theArchivalLibrary/v1/file")
public class DocumentController {



    @PostMapping
    public void uploadFile(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("dataOfPublication") String dataOfPublication,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Data of Publication: " + dataOfPublication);
        System.out.println("Description: " + description);
        System.out.println("File name: " + file.getOriginalFilename());

        if (!file.isEmpty()) {
            // Specify the upload directory
            String uploadDir = "C:\\Users\\Saami\\IdeaProjects\\archivalLibrary\\src\\main\\resources\\"; // Ensure trailing slash

            // Create the destination file path with the proper separator
            File destFile = new File(uploadDir + File.separator + file.getOriginalFilename());

            try {
                // Save the file
                file.transferTo(destFile);
                System.out.println("File uploaded successfully to: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to upload file: " + e.getMessage());
            }
        } else {
            System.out.println("File is empty. Cannot upload.");
        }
    }

}

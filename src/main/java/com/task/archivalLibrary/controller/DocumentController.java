package com.task.archivalLibrary.controller;

import com.task.archivalLibrary.DTO.DocumentDTO;
import com.task.archivalLibrary.DTO.UserDTO;
import com.task.archivalLibrary.entity.Document;
import com.task.archivalLibrary.entity.User;
import com.task.archivalLibrary.repository.DocumentsRepository;
import com.task.archivalLibrary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/theArchivalLibrary/v1/file")
public class DocumentController {

    private final DocumentsRepository documentsRepository;
    private final UserRepository userRepository;


    @PostMapping
    public void uploadFile(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("dataOfPublication") String dataOfPublication,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) throws IOException {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Data of Publication: " + dataOfPublication);
        System.out.println("Description: " + description);
        System.out.println("User ID: " + userId);
        System.out.println("----------");
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());
        System.out.println(file.getSize());
        System.out.println(file.getName());
        System.out.println(file.getResource());


        if (!file.isEmpty()) {
            // Define the base upload directory
            Path baseUploadDir = Paths.get("src/main/resources", userId);

            // Check if the user directory exists, create if not
            if (!Files.exists(baseUploadDir)) {
                Files.createDirectories(baseUploadDir);
            }

            // Define the path for the file to be saved
            Path filePath = baseUploadDir.resolve(file.getOriginalFilename());

            // Write the file to the directory
            Files.write(filePath, file.getBytes());
            User user = userRepository.findById(userId).get();

            System.out.println(filePath.toString());
            Document document = new Document();
            document.setTitle(title);
            document.setAuthor(author);
            document.setDataOfPublication(dataOfPublication);
            document.setUser(user);
            document.setDescription(description);
            document.setFilePath(filePath.toString());
            documentsRepository.save(document);


            System.out.println("File uploaded to: " + filePath.toString());
        }
    }

    @GetMapping("/{userId}")
    public List<DocumentDTO> getAllDocuments(@PathVariable String userId) {
        User userAA = userRepository.findById(userId).get();
        List<Document> documents = documentsRepository.findAllByUser(userAA);
        List<DocumentDTO> documentDTOList = new ArrayList<>();

        documents.forEach((document -> {
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setUuid(document.getUuid());
            documentDTO.setTitle(document.getTitle());
            documentDTO.setAuthor(document.getAuthor());
            documentDTO.setDataOfPublication(document.getDataOfPublication());
            documentDTO.setDescription(document.getDescription());
            documentDTO.setFilePath(document.getFilePath());
            documentDTOList.add(documentDTO);
        }));

        return documentDTOList;
    }

    @DeleteMapping
    public void deleteDocument(@RequestBody DocumentDTO documentDTO) throws IOException {
        System.out.println(documentDTO.getUuid());
        System.out.println(documentDTO.getFilePath());
        Path path = Paths.get(documentDTO.getFilePath());
        Files.delete(path);


        documentsRepository.deleteById(documentDTO.getUuid());




    }

}

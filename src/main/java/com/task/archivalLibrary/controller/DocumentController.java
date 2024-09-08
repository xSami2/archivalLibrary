package com.task.archivalLibrary.controller;

import com.task.archivalLibrary.DTO.DocumentDTO;
import com.task.archivalLibrary.entity.Document;
import com.task.archivalLibrary.entity.User;
import com.task.archivalLibrary.repository.DocumentsRepository;
import com.task.archivalLibrary.repository.UserRepository;
import com.task.archivalLibrary.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/theArchivalLibrary/v1/file")
public class DocumentController {


    private final DocumentService documentService;


    @PostMapping
    public ResponseEntity<DocumentDTO> uploadFile(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("dataOfPublication") String dataOfPublication,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) {
      return documentService.uploadFile(title, author, dataOfPublication, description, file, userId);
    }




    @GetMapping("/{userId}")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments(@PathVariable String userId) {
       return documentService.getAllDocuments(userId);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDocument(@RequestBody DocumentDTO documentDTO) throws IOException {
        return documentService.deleteDocument(documentDTO);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {
       return documentService.downloadFile(path);
    }

}

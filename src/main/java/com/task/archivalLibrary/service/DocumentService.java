package com.task.archivalLibrary.service;

import com.task.archivalLibrary.DTO.DocumentDTO;
import com.task.archivalLibrary.entity.Document;
import com.task.archivalLibrary.entity.User;
import com.task.archivalLibrary.mapper.DocumentMapper;
import com.task.archivalLibrary.repository.DocumentsRepository;
import com.task.archivalLibrary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentService {

  private final DocumentsRepository documentsRepository;
  private final UserRepository userRepository;
  private final DocumentMapper documentMapper;
  private final String fileLocation;



  public ResponseEntity<DocumentDTO> uploadFile(
      String title,
      String author,
      String dataOfPublication,
      String description,
      MultipartFile file,
      String userId) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body(null);
      }

      Optional<User> optionalUser = userRepository.findById(userId);
      if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      User user = optionalUser.get();

      Path filePath = saveFile(file, userId);

      Document newDocument =
          new Document(
              title,
              author,
              user,
              description,
              dataOfPublication,
              filePath.toString(),
              file.getOriginalFilename());
      Document savedDocument = documentsRepository.save(newDocument);

      DocumentDTO documentDTO = documentMapper.toDto(savedDocument);
      System.out.println(documentDTO);
      return ResponseEntity.ok(documentDTO);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  public ResponseEntity<List<DocumentDTO>> getAllDocuments(String userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    User user = optionalUser.get();

    List<Document> documents = documentsRepository.findAllByUser(user);

    List<DocumentDTO> documentDTOs = documentMapper.toDtoList(documents);

    return ResponseEntity.ok(documentDTOs);
  }

  public ResponseEntity<String> deleteDocument( DocumentDTO documentDTO) throws IOException {
    Path path = Paths.get(documentDTO.getFilePath());
    Files.delete(path);
    documentsRepository.deleteById(documentDTO.getUuid());
    return ResponseEntity.ok("Deleted Document");
  }

 public ResponseEntity<Resource> downloadFile(String path){
    try {
      Path filePath = Paths.get(path);

      if (Files.exists(filePath)) {
        Resource resource = new UrlResource(filePath.toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getFileName());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(filePath)));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Path saveFile(MultipartFile file, String userId) throws IOException {
    System.out.println( "SSSSSSS" +fileLocation);
    Path baseUploadDir = Paths.get(fileLocation, userId);

    if (!Files.exists(baseUploadDir)) {
      Files.createDirectories(baseUploadDir);
    }
    Path filePath = baseUploadDir.resolve(file.getOriginalFilename());
    Files.write(filePath, file.getBytes());

    return filePath;
  }

}

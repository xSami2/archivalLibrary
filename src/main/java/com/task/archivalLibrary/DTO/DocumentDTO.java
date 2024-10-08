package com.task.archivalLibrary.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentDTO {


    private String uuid;
    private String title;
    private String author;
    private String description;
    private String dataOfPublication;
    private String filePath;
    private String fileName;



}

package com.task.archivalLibrary.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentDTO {


    private String title;
    private String author;
    private String description;
    private String dataOfPublication;
    private MultipartFile file;


}

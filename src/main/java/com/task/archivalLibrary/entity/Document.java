package com.task.archivalLibrary.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid ;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String author;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 500)
    private String description;

    @Column(name = "data_of_publication")
    private String dataOfPublication;

    @Column(name = "file_path")
    private String filePath;


    // Getters and Setters
}
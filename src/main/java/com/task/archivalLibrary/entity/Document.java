package com.task.archivalLibrary.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "documents")
@NoArgsConstructor
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

    @Column(name = "file_name")
    private String fileName;

    public Document(String title, String author, User user, String description, String dataOfPublication, String filePath ,String fileName) {
        this.title = title;
        this.author = author;
        this.user = user;
        this.description = description;
        this.dataOfPublication = dataOfPublication;
        this.filePath = filePath;
        this.fileName = fileName;
    }

}
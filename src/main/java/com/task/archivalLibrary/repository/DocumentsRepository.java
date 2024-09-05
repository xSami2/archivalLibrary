package com.task.archivalLibrary.repository;

import com.task.archivalLibrary.entity.Document;
import com.task.archivalLibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<Document, String> {

    List<Document> findAllByUser(User user);

}

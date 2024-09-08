package com.task.archivalLibrary.mapper;

import com.task.archivalLibrary.DTO.DocumentDTO;
import com.task.archivalLibrary.entity.Document;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DocumentMapper {

    private final ModelMapper modelMapper;


    public DocumentDTO toDto(Document document) {
        return modelMapper.map(document, DocumentDTO.class);
    }

    public Document toEntity(DocumentDTO documentDTO) {
        return modelMapper.map(documentDTO, Document.class);
    }

    public List<DocumentDTO> toDtoList(List<Document> documents) {
        return documents.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Document> toEntityList(List<DocumentDTO> documentDTOs) {
        return documentDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

}

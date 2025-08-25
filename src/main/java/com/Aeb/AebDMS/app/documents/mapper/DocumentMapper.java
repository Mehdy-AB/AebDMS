package com.Aeb.AebDMS.app.documents.mapper;

import com.Aeb.AebDMS.app.documents.dto.res.DocumentResponseDto;
import com.Aeb.AebDMS.app.documents.model.Document;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;

import com.Aeb.AebDMS.app.documents.repository.DocumentRepository;
import com.Aeb.AebDMS.app.folders.repository.FolderRepository;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    final private IKeycloakUserService keycloakUserService;
    final private FolderRepository folderRepository;
    //private final Document
    private final DocumentRepository documentRepository;

    public DocumentResponseDto toDocumentUploadResponseDto(DocumentVersion version) {
        Document document = version.getDocument();
        Long folderId = document.getFolder().getId();
        Optional<UserDto> owner = keycloakUserService.getUserById(document.getOwnedBy());
        Optional<UserDto> creator = keycloakUserService.getUserById(document.getCreatedBy());
        String path = folderRepository.getFolderPath(folderId);
        List<String> categories = documentRepository.getDocumentCategories(document.getId());
        return DocumentResponseDto.builder()
                .documentId(document.getId())
                .versionId(version.getId())
                .updatedAt(document.getUpdatedAt())
                .createdBy(creator.orElse(null))
                .ownedBy(owner.orElse(null))
                .name(document.getName())
                .folderId(folderId)
                .path(path)
                .sizeBytes(version.getSizeBytes())
                .mimeType(version.getMimeType())
                .versionNumber(version.getVersionNumber())
                .createdAt(document.getCreatedAt())
                .isPublic(document.isPublic())
                .metadata(categories)
                .build();
    }

    public DocumentResponseDto toDocumentUploadResponseDto(DocumentVersion version,Map<Long, String> paths, Map<String, UserDto> users) {
        Document document = version.getDocument();
        Long folderId = document.getFolder().getId();
        List<String> categories = documentRepository.getDocumentCategories(document.getId());
        return DocumentResponseDto.builder()
                .documentId(document.getId())
                .versionId(version.getId())
                .updatedAt(document.getUpdatedAt())
                .isPublic(document.isPublic())
                .createdBy(users.get(document.getCreatedBy()))
                .ownedBy(users.get(document.getOwnedBy()))
                .name(document.getName())
                .folderId(folderId)
                .path(paths.get(folderId))
                .sizeBytes(version.getSizeBytes())
                .mimeType(version.getMimeType())
                .versionNumber(version.getVersionNumber())
                .createdAt(document.getCreatedAt())
                .metadata(categories)
                .build();
    }

    public DocumentResponseDto toDocumentUploadResponseDto(DocumentVersion version, String path, Map<String, UserDto> users) {
        Document document = version.getDocument();
        Long folderId = document.getFolder().getId();
        List<String> categories = documentRepository.getDocumentCategories(document.getId());
        return DocumentResponseDto.builder()
                .documentId(document.getId())
                .versionId(version.getId())
                .updatedAt(document.getUpdatedAt())
                .isPublic(document.isPublic())
                .createdBy(users.get(document.getCreatedBy()))
                .ownedBy(users.get(document.getOwnedBy()))
                .name(document.getName())
                .folderId(folderId)
                .path(path)
                .sizeBytes(version.getSizeBytes())
                .mimeType(version.getMimeType())
                .versionNumber(version.getVersionNumber())
                .createdAt(document.getCreatedAt())
                .metadata(categories)
                .build();
    }
}

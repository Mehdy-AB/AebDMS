package com.Aeb.AebDMS.app.folders.mapper;

import com.Aeb.AebDMS.app.documents.mapper.DocumentMapper;
import com.Aeb.AebDMS.app.folders.dto.app.FolderGetResApp;
import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.dto.res.FolderResDto;
import com.Aeb.AebDMS.app.folders.dto.res.FolderRepoResDto;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.repository.FolderRepository;
import com.Aeb.AebDMS.app.user.dto.UserDto;
import com.Aeb.AebDMS.app.user.service.IKeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FolderMapper {

    final private IKeycloakUserService keycloakUserService;
    private final FolderRepository folderRepo;
    private final DocumentMapper documentMapper;

    public FolderResDto toResponseDto(Folder folder){
        Optional<UserDto> owner = keycloakUserService.getUserById(folder.getOwnedBy());
        Optional<UserDto> creator = keycloakUserService.getUserById(folder.getCreatedBy());

        return FolderResDto.builder()
                .id(folder.getId())
                .name(folder.getName())
                .description(folder.getDescription())
                .path(folder.getPath())
                .parentId(folder.getParentId())
                .ownedBy(owner.orElse(null))
                .createdBy(creator.orElse(null))
                .size(folderRepo.countSubFoldersAndDocuments(folder.getId()))
                .isPublic(folder.isPublic())
                .createdAt(folder.getCreatedAt())
                .updatedAt(folder.getUpdatedAt())
                .build();
    }

    public FolderResDto toManyResponseDto(FolderWithCountDto folder,Map<String, UserDto> users){

        return FolderResDto.builder()
                .id(folder.getFolder().getId())
                .name(folder.getFolder().getName())
                .description(folder.getFolder().getDescription())
                .path(folder.getFolder().getPath())
                .parentId(folder.getFolder().getParentId())
                .ownedBy(users.get(folder.getFolder().getOwnedBy()))
                .createdBy(users.get(folder.getFolder().getCreatedBy()))
                .size((long) folder.getCount())
                .isPublic(folder.getFolder().isPublic())
                .createdAt(folder.getFolder().getCreatedAt())
                .updatedAt(folder.getFolder().getUpdatedAt())
                .build();
    }

    public FolderRepoResDto toFolderRepoResponse(FolderGetResApp appRes){
        Set<String> ids = new HashSet<>();
        appRes.getFolders().forEach(folderWithCountDto -> {
            ids.add(folderWithCountDto.getFolder().getCreatedBy());
            ids.add(folderWithCountDto.getFolder().getOwnedBy());
        });
        appRes.getDocuments().forEach(d -> {
            ids.add(d.getDocument().getCreatedBy());
            ids.add(d.getDocument().getOwnedBy());
        });
        Map<String, UserDto> users = keycloakUserService.getUsersByIds(ids);

        return FolderRepoResDto.builder()
                .folder(toResponseDto(appRes.getFolder()))
                .folders(appRes.getFolders().stream().map(folderWithCountDto -> toManyResponseDto(folderWithCountDto,users)).toList())
                .documents(appRes.getDocuments().stream().map(doc->documentMapper.toDocumentUploadResponseDto(doc,appRes.getFolder().getPath(),users)).toList())
                .pageable(appRes.getPageable())
                .build();
    }

    public Page<FolderResDto> toMyRepoResponse(Page<FolderWithCountDto> folderWithCountDto){
        Set<String> ids = new HashSet<>();
        folderWithCountDto.getContent().forEach(f -> {
            ids.add(f.getFolder().getCreatedBy());
            ids.add(f.getFolder().getOwnedBy());
        });

        Map<String, UserDto> users = keycloakUserService.getUsersByIds(ids);

        return new PageImpl<>(folderWithCountDto.getContent().stream().map(f->toManyResponseDto(f,users)).toList(),
                              folderWithCountDto.getPageable(), folderWithCountDto.getTotalElements());
    }
}

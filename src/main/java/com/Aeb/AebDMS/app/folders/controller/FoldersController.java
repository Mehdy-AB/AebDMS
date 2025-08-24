package com.Aeb.AebDMS.app.folders.controller;

import com.Aeb.AebDMS.app.folders.dto.app.FolderGetResApp;
import com.Aeb.AebDMS.app.folders.dto.app.FolderWithCountDto;
import com.Aeb.AebDMS.app.folders.dto.req.CreateFolderDto;
import com.Aeb.AebDMS.app.folders.dto.req.TypeShareAccessWithTypeReq;
import com.Aeb.AebDMS.app.folders.dto.res.FolderResDto;
import com.Aeb.AebDMS.app.folders.dto.res.FolderRepoResDto;
import com.Aeb.AebDMS.app.folders.dto.res.TypeShareAccessRes;
import com.Aeb.AebDMS.app.folders.mapper.FolderMapper;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.app.folders.service.IFolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${app.v1.path}/folder")
@RequiredArgsConstructor
public class FoldersController {
    final private IFolderService folderService;
    private final FolderMapper folderMapper;

    @PostMapping
    public ResponseEntity<FolderResDto> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateFolderDto createFolderDto) {
        Folder folder = folderService.createFolder(createFolderDto,jwt.getSubject());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                folderMapper.toResponseDto(folder)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderRepoResDto> getFolder(@AuthenticationPrincipal Jwt jwt, @NonNull @PathVariable("id") Long id,
                                                      @RequestParam(value = "page",defaultValue = "0") Integer page, @RequestParam(value = "size",defaultValue = "20") Integer size) {

        FolderGetResApp folder = folderService.getFullFolder(id,jwt.getSubject(), PageRequest.of(page, size));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                folderMapper.toFolderRepoResponse(folder)
        );
    }

    @GetMapping()
    public ResponseEntity<Page<FolderResDto>> getRepo(@AuthenticationPrincipal Jwt jwt,
                                                    @RequestParam(value = "page",defaultValue = "0") Integer page, @RequestParam(value = "size",defaultValue = "20") Integer size) {

        Page<FolderWithCountDto> folder = folderService.getMyRepo(jwt.getSubject(), PageRequest.of(page, size));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                folderMapper.toMyRepoResponse(folder)
        );
    }

    @PutMapping("/rename/{id}")
    public ResponseEntity<Void> reName(@AuthenticationPrincipal Jwt jwt,
                                        @NonNull @PathVariable("id") Long id,
                                        @RequestParam(value = "name",required = true) String name) {

        folderService.reName(id,jwt.getSubject(),name);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/move/{id}/{to}")
    public ResponseEntity<Void> move(@AuthenticationPrincipal Jwt jwt,
                                        @NonNull @PathVariable("id") Long id,
                                        @NonNull @PathVariable("to") Long to ){

        folderService.move(id,jwt.getSubject(),to);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/share")
    public ResponseEntity<Page<TypeShareAccessRes>> getShared(@AuthenticationPrincipal Jwt jwt,
                                                         @NonNull @PathVariable("id") Long id,
                                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                                         @RequestParam(value = "size",defaultValue = "20") Integer size){


        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.getAllPermission(id, jwt.getSubject(), PageRequest.of(page, size)));
    }

    @PostMapping("/{folderId}/share")
    public ResponseEntity<TypeShareAccessRes> createOrUpdateShared(@AuthenticationPrincipal Jwt jwt,
                                                                         @NonNull @PathVariable("folderId") Long folderId,
                                                                         @NonNull @Valid @RequestBody TypeShareAccessWithTypeReq data){


        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.createOrUpdatePermission(folderId, jwt.getSubject(), data.getType(),data.getGranteeId(),data.getPermission()));
    }

    @DeleteMapping("/{folderId}/share/{granteeId}")
    public ResponseEntity<Void> deleteShared(@AuthenticationPrincipal Jwt jwt,
                                                                 @NonNull @PathVariable("folderId") Long folderId,
                                                                 @NonNull @PathVariable("granteeId") String granteeId,
                                                                 @RequestParam(value = "inherits") boolean inherits){

        folderService.deletePermission(folderId, jwt.getSubject(), granteeId,inherits);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> move(@AuthenticationPrincipal Jwt jwt,
                                     @NonNull @PathVariable("id") Long id ){

        folderService.deleteById(id,jwt.getSubject());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

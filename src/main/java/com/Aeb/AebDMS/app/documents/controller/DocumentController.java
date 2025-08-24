package com.Aeb.AebDMS.app.documents.controller;

import com.Aeb.AebDMS.app.documents.dto.req.DocumentUploadRequestDto;
import com.Aeb.AebDMS.app.documents.dto.req.FilingCategoryDocDto;
import com.Aeb.AebDMS.app.documents.dto.req.TypeShareAccessDocWithTypeReq;
import com.Aeb.AebDMS.app.documents.dto.res.DocumentResponseDto;
import com.Aeb.AebDMS.app.documents.dto.res.TypeShareAccessDocumentRes;
import com.Aeb.AebDMS.app.documents.mapper.DocumentMapper;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.documents.service.IDocumentService;
import com.Aeb.AebDMS.app.folders.dto.req.TypeShareAccessWithTypeReq;
import com.Aeb.AebDMS.app.folders.dto.res.TypeShareAccessRes;
import com.Aeb.AebDMS.shared.util.ExtractorLanug;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${app.v1.path}/document")
@RequiredArgsConstructor
public class DocumentController {

    private final IDocumentService documentService;
    private final DocumentMapper documentMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDto> upload(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("folderId") @NotNull Long folderId,
            @RequestParam("lang") @NotNull ExtractorLanug lang,
            @RequestPart("filingCategory") List<FilingCategoryDocDto> filingCategoryDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        DocumentUploadRequestDto request = DocumentUploadRequestDto.builder()
                .file(file)
                .folderId(folderId)
                .lang(lang)
                .createdBy(jwt.getSubject())
                .build();
        DocumentVersion doc = documentService.uploadDocument(request,filingCategoryDto);
        DocumentResponseDto response =  documentMapper.toDocumentUploadResponseDto(doc);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/rename/{id}")
    public ResponseEntity<Void> reName(@AuthenticationPrincipal Jwt jwt,
                                       @NonNull @PathVariable("id") Long id,
                                       @RequestParam(value = "name",required = true) String name) {

        documentService.reName(id,jwt.getSubject(),name);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/move/{id}/{to}")
    public ResponseEntity<Void> move(@AuthenticationPrincipal Jwt jwt,
                                     @NonNull @PathVariable("id") Long id,
                                     @NonNull @PathVariable("to") Long to ){

        documentService.move(id,jwt.getSubject(),to);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/share")
    public ResponseEntity<Page<TypeShareAccessDocumentRes>> getShared(@AuthenticationPrincipal Jwt jwt,
                                                                      @NonNull @PathVariable("id") Long id,
                                                                      @RequestParam(value = "page",defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size",defaultValue = "20") Integer size){


        return ResponseEntity.status(HttpStatus.OK)
                .body(documentService.getAllPermission(id, jwt.getSubject(), PageRequest.of(page, size)));
    }

    @PostMapping("/{folderId}/share")
    public ResponseEntity<TypeShareAccessDocumentRes> createOrUpdateShared(@AuthenticationPrincipal Jwt jwt,
                                                                   @NonNull @PathVariable("folderId") Long folderId,
                                                                   @NonNull @Valid @RequestBody TypeShareAccessDocWithTypeReq data){


        return ResponseEntity.status(HttpStatus.OK)
                .body(documentService.createOrUpdatePermission(folderId, jwt.getSubject(), data.getType(),data.getGranteeId(),data.getPermission()));
    }

    @DeleteMapping("/{folderId}/share/{granteeId}")
    public ResponseEntity<Void> deleteShared(@AuthenticationPrincipal Jwt jwt,
                                             @NonNull @PathVariable("folderId") Long folderId,
                                             @NonNull @PathVariable("granteeId") String granteeId){

        documentService.deletePermission(folderId, jwt.getSubject(), granteeId);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> move(@AuthenticationPrincipal Jwt jwt,
                                     @NonNull @PathVariable("id") Long id ){

        documentService.deleteById(id,jwt.getSubject());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

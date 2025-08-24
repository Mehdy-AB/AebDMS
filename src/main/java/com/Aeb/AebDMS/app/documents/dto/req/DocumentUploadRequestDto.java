package com.Aeb.AebDMS.app.documents.dto.req;


import com.Aeb.AebDMS.shared.util.ExtractorLanug;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class DocumentUploadRequestDto {
    private MultipartFile file;
    private Long folderId;
    private String createdBy;
    private ExtractorLanug lang;
}
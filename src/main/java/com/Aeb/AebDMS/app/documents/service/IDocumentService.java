package com.Aeb.AebDMS.app.documents.service;

import com.Aeb.AebDMS.app.documents.dto.req.DocumentPermissionReq;
import com.Aeb.AebDMS.app.documents.dto.req.DocumentUploadRequestDto;
import com.Aeb.AebDMS.app.documents.dto.req.FilingCategoryDocDto;
import com.Aeb.AebDMS.app.documents.dto.req.TypeShareAccesDoc;
import com.Aeb.AebDMS.app.documents.dto.res.TypeShareAccessDocumentRes;
import com.Aeb.AebDMS.app.documents.model.Document;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.folders.dto.res.TypeShareAccessRes;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.shared.util.GranteeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDocumentService {

    DocumentVersion uploadDocument(DocumentUploadRequestDto document, List<FilingCategoryDocDto> filingCategories);

    List<Document> findAll();
    
    Page<Document> findAll(Pageable pageable);

    DocumentVersion findById(Long id,String userId);
    
    Document updateDocument(Document document);
    boolean canRead(Folder folder,String userId,Long docId);

    DocumentVersion updateDocumentVersion(DocumentVersion document);

    void deleteById(Long id);

    void reName(Long id, String userId, String name);

    void move(Long id, String userId, Long desId);

    @Transactional
    void deleteById(Long id, String userId);

    Page<TypeShareAccessDocumentRes> getAllPermission(long documentId, String userId, Pageable page);

    @Transactional
    TypeShareAccessDocumentRes createOrUpdatePermission(long documentId, String userId, GranteeType granteeType,
                                                        String granteeId, DocumentPermissionReq permissionReq);

    void deletePermission(long documentId, String userId, String granteeId);
}

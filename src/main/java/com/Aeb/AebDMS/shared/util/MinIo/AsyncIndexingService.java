package com.Aeb.AebDMS.shared.util.MinIo;

import com.Aeb.AebDMS.app.documents.dto.MetadataTransferee;
import com.Aeb.AebDMS.app.documents.model.DocumentVersion;
import com.Aeb.AebDMS.app.elastic.model.DocumentVersionDocumentElastic;
import com.Aeb.AebDMS.app.elastic.model.MetadataDocumentElastic;
import com.Aeb.AebDMS.app.elastic.service.IDocumentVersionDocumentServiceElastic;
import com.Aeb.AebDMS.app.folders.model.Folder;
import com.Aeb.AebDMS.shared.util.ExtractorLanug;
import com.Aeb.AebDMS.shared.util.HybridTextExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncIndexingService {
    private final IDocumentVersionDocumentServiceElastic documentVersionDocumentServiceElastic;
    private final HybridTextExtractionService textExtraction;
    private final MinioService minioService;


    @Async
    public void processAsyncIndexing(String ownerId, String uploadedFile, String originalFilename, String contentType, ExtractorLanug lang, List<MetadataTransferee> newCategories, DocumentVersion version, Folder folder) {
        try {
            InputStream file = minioService.getObjectStream(uploadedFile);
            String ocrText = textExtraction.extractText(file,originalFilename, contentType, lang);
            //System.out.println(ocrText);
            //metadataDocumentServiceElastic.saveAllMetadataDocument(newCategories);

            List <MetadataDocumentElastic> metadataDocument =  newCategories.stream().map(metadataTransferee -> MetadataDocumentElastic
                            .builder()
                            .metadataId(metadataTransferee.getMetadataId())
                            .categoryId(metadataTransferee.getCategoryId())
                            .value(metadataTransferee.getValue())
                            .categoryName(metadataTransferee.getCategoryName())
                            .key(metadataTransferee.getMetaDataName())
                            .build())
                    .toList();

            documentVersionDocumentServiceElastic.saveDocumentVersionDocument(
                    DocumentVersionDocumentElastic.builder()
                            .id(version.getDocument().getId())
                            .folderId(version.getDocument().getFolderId())
                            .ocrText(ocrText)
                            .sizeBytes(version.getSizeBytes())
                            .mimeType(version.getMimeType())
                            .path(folder.getPath())
                            .name(originalFilename)
                            .grantedIds(List.of(ownerId))
                            .createdAt(version.getDocument().getCreatedAt())
                            .updatedAt(version.getDocument().getUpdatedAt())
                            .metadata(metadataDocument)
                            .build()
            );
        } catch (Exception e) {

            System.err.println("Async OCR/Indexing failed: " + e.getMessage());
        }
    }
}

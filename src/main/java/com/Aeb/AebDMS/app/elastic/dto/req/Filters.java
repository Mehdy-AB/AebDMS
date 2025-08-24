package com.Aeb.AebDMS.app.elastic.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filters {

    // Fields to include in search
    private Boolean lookUpFolderName;
    private Boolean lookUpDocumentName;
    private Boolean lookUpMetadataKey;
    private Boolean lookUpMetadataValue;
    private Boolean lookUpCategoryName;
    private Boolean lookUpOcrContent;
    private Boolean lookUpDescription;

    // Filter by type
    private Boolean includeFolders;
    private Boolean includeDocuments;

    // Sorting
    private SortFields sortBy; // "score", "name", "createdAt", "updatedAt"
    private Boolean sortDesc; // true = descending, false = ascending
}


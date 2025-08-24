package com.Aeb.AebDMS.app.folders.dto.app;

import com.Aeb.AebDMS.app.folders.model.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderWithCountDto {
    private Folder folder;
    private Long countFolders;
    private Long countDocuments;

    public Long getCount(){
        return countDocuments+countFolders;
    }
}

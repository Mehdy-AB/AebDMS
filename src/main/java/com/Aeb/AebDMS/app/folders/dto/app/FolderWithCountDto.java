package com.Aeb.AebDMS.app.folders.dto.app;

import com.Aeb.AebDMS.app.folders.model.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderWithCountDto {
    private Folder folder;
    private int countFolders;
    private int countDocuments;

    public Long getCount(){
        return (long) (countDocuments+countFolders);
    }
}

package com.virtualfilesystem.VirtualFileSystem.domain.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.File;

import java.util.List;

public interface FileRepository {
    void saveFile(File file);
    List<File> getAllFiles();
    File getFileByPath(String path);
}

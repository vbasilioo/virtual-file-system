package com.virtualfilesystem.VirtualFileSystem.domain.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.File;

import java.util.List;

public interface FileRepository {
    File saveFile(File file);
    void delete(File file);
    List<File> getAllFiles();
    File getFileByPath(String path);
}

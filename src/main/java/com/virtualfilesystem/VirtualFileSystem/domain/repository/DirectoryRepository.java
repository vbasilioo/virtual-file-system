package com.virtualfilesystem.VirtualFileSystem.domain.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;

import java.util.List;

public interface DirectoryRepository {
    void saveDirectory(Directory directory);
    List<Directory> getAllDirectories();
    Directory getDirectoryByPath(String path);
}

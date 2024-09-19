package com.virtualfilesystem.VirtualFileSystem.domain.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepository {
    void saveDirectory(Directory directory);
    List<Directory> getAllDirectories();
    Directory getDirectoryByPath(String path);
    void delete(Directory directory);
    Optional<Directory> findById(Long id);
}

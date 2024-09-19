package com.virtualfilesystem.VirtualFileSystem.infrastructure.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDirectoryRepository extends DirectoryRepository, JpaRepository<Directory, Long> {
    @Override
    default void saveDirectory(Directory directory) {
        save(directory);
    }

    @Override
    default List<Directory> getAllDirectories(){
        return findAll();
    }

    @Override
    default Directory getDirectoryByPath(String path) {
        return findByPath(path);
    }

    Directory findByPath(String path);
}

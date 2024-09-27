package com.virtualfilesystem.VirtualFileSystem.infrastructure.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.FileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaFileRepository extends FileRepository, JpaRepository<File, UUID> {
    @Override
    default File saveFile(File file) {
        return save(file);
    }

    @Override
    default void delete(File file) {
        delete(file);
    }

    @Override
    default List<File> getAllFiles() {
        return findAll();
    }

    @Override
    default File getFileByPath(String path) {
        return findByPath(path);
    }

    File findByPath(String path);
}

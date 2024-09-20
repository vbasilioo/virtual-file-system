package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;
    private final FileService fileService;

    public DirectoryService(DirectoryRepository directoryRepository, FileService fileService) {
        this.directoryRepository = directoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public Directory saveDirectory(Directory directory) {
        if (directory.getParent() != null) {
            Directory parent = directoryRepository.getDirectoryByPath(directory.getParent().getPath());
            directory.setParent(parent);
        }
        return saveOrUpdateDirectory(directory);
    }

    public Directory saveOrUpdateDirectory(Directory directory) {
        directoryRepository.saveDirectory(directory);
        for (Directory child : directory.getChildren()) {
            child.setParent(directory);
            saveOrUpdateDirectory(child);
        }
        return directory;
    }

    @Transactional
    public void deleteDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Directory not found"));

        fileService.deleteFilesByDirectory(directory);

        for (Directory child : directory.getChildren()) {
            deleteDirectory(child.getId());
        }

        directoryRepository.delete(directory);
    }

    public List<Directory> getAllDirectories() {
        return directoryRepository.getAllDirectories();
    }

    public Directory getDirectoryByPath(String path) {
        return directoryRepository.getDirectoryByPath(path);
    }
}

package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            if (parent != null) {
                directory.setParent(parent);
            } else {
                throw new ApiException("Parent directory not found for path: " + directory.getParent().getPath());
            }
        }

        directoryRepository.saveDirectory(directory);

        for (Directory child : directory.getChildren()) {
            child.setParent(directory);

            Directory existingChild = directoryRepository.getDirectoryByPath(child.getPath());
            if (existingChild == null) {
                directoryRepository.saveDirectory(child);
            } else {
                child.setId(existingChild.getId());
                directoryRepository.saveDirectory(child);
            }
        }

        return directory;
    }

    public List<Directory> getAllDirectories() {
        List<Directory> allDirectories = directoryRepository.getAllDirectories();

        return allDirectories.stream()
                .filter(directory -> directory.getParent() == null)
                .collect(Collectors.toList());
    }

    public Directory getDirectoryByPath(String path) {
        return directoryRepository.getDirectoryByPath(path);
    }

    @Transactional
    public void deleteDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com ID: " + id));

        deleteRecursively(directory);

        directoryRepository.deleteById(id);
    }

    private void deleteRecursively(Directory directory) {
        for (Directory child : directory.getChildren())
            deleteRecursively(child);
    }
}

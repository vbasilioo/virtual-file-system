package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.repository.JpaFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectoryService {
    @Autowired
    DirectoryRepository directoryRepository;
    @Autowired
    FileService fileService;
    @Autowired
    JpaFileRepository jpaFileRepository;

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

    public Map<String, Long> getOverviewStatistics(){
        long totalDirectories = getAllDirectories().size();
        long totalFiles = jpaFileRepository.getAllFiles().size();

        return Map.of("Directories", totalDirectories, "Files", totalFiles);
    }

    public long getFileCountInDirectory(Long id){
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com o ID: " + id));

        return jpaFileRepository.getAllFiles().stream()
                .filter(file -> file.getDirectory().equals(directory))
                .count();
    }

    public Map<Long, Long> getTotalFileSizeByDirectory(){
        Map<Long, Long> totalSizeByDirectory = new HashMap<>();

        List<File> files = jpaFileRepository.getAllFiles();
        for(File file : files) {
            totalSizeByDirectory.merge(
                    file.getDirectory().getId(),
                    file.getSize(),
                    Long::sum
            );
        }

        return totalSizeByDirectory;
    }
}

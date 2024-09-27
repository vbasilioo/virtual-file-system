package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.repository.JpaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private JpaFileRepository jpaFileRepository;

    @Transactional
    public Directory saveDirectory(Directory directory) {
        // Verifica e associa o diretório pai
        if (directory.getParent() != null) {
            Directory parent = directoryRepository.getDirectoryByPath(directory.getParent().getPath());
            if (parent != null) {
                directory.setParent(parent);
            } else {
                throw new ApiException("Parent directory not found for path: " + directory.getParent().getPath());
            }
        }

        directoryRepository.saveDirectory(directory);

        // Processa diretórios filhos
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

        // Filtra apenas diretórios raiz (sem parent)
        return allDirectories.stream()
                .filter(directory -> directory.getParent() == null)
                .collect(Collectors.toList());
    }

    public Directory getDirectoryByPath(String path) {
        Directory directory = directoryRepository.getDirectoryByPath(path);
        if (directory == null) {
            throw new ApiException("Diretório não encontrado para o caminho: " + path);
        }
        return directory;
    }

    @Transactional
    public void deleteDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com o ID: " + id));

        // Deleta recursivamente todos os filhos
        deleteRecursively(directory);

        directoryRepository.deleteById(id);
    }

    private void deleteRecursively(Directory directory) {
        for (Directory child : directory.getChildren()) {
            deleteRecursively(child);
        }
        directoryRepository.deleteById(directory.getId());
    }

    public Map<String, Long> getOverviewStatistics() {
        long totalDirectories = getAllDirectories().size();
        long totalFiles = jpaFileRepository.getAllFiles().size();

        return Map.of("Directories", totalDirectories, "Files", totalFiles);
    }

    public long getFileCountInDirectory(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com o ID: " + id));

        return jpaFileRepository.getAllFiles().stream()
                .filter(file -> file.getDirectory().equals(directory))
                .count();
    }

    public Map<Long, Long> getTotalFileSizeByDirectory() {
        Map<Long, Long> totalSizeByDirectory = new HashMap<>();

        List<File> files = jpaFileRepository.getAllFiles();
        for (File file : files) {
            totalSizeByDirectory.merge(
                    file.getDirectory().getId(),
                    file.getSize(),
                    Long::sum
            );
        }

        return totalSizeByDirectory;
    }
}

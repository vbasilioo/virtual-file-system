package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.DTO.Directory.DirectoryDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.model.User;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.repository.JpaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private JpaFileRepository jpaFileRepository;

    @Transactional
    public DirectoryDTO saveDirectory(DirectoryDTO directoryDTO, User user) {
        Directory directory = new Directory(directoryDTO.path(), directoryDTO.name(), user);
        if (directoryDTO.parentId() != null) {
            Directory parent = directoryRepository.findById(directoryDTO.parentId())
                    .orElseThrow(() -> new ApiException("Diretório pai não encontrado para ID: " + directoryDTO.parentId()));
            directory.setParent(parent);
        }

        directoryRepository.saveDirectory(directory);

        for (UUID childId : directoryDTO.childrenIds()) {
            Directory child = directoryRepository.findById(childId)
                    .orElseThrow(() -> new ApiException("Diretório filho não encontrado com ID: " + childId));
            child.setParent(directory);
            directoryRepository.saveDirectory(child);
        }

        return new DirectoryDTO(
                directory.getId(),
                directory.getName(),
                directory.getPath(),
                directory.getParent() != null ? directory.getParent().getId() : null,
                directoryDTO.childrenIds()
        );
    }

    public List<DirectoryDTO> getAllDirectories() {
        List<Directory> allDirectories = directoryRepository.getAllDirectories();

        return allDirectories.stream()
                .filter(directory -> directory.getParent() == null)
                .map(directory -> {
                    List<UUID> childrenIds = directory.getChildren().stream()
                            .map(Directory::getId)
                            .collect(Collectors.toList());

                    return new DirectoryDTO(
                            directory.getId(),
                            directory.getName(),
                            directory.getPath(),
                            null,
                            childrenIds
                    );
                })
                .collect(Collectors.toList());
    }


    public DirectoryDTO getDirectoryByPath(String path) {
        Directory directory = directoryRepository.getDirectoryByPath(path);
        if (directory == null) {
            throw new ApiException("Diretório não encontrado para o caminho: " + path);
        }

        List<UUID> childrenIds = directory.getChildren().stream()
                .map(Directory::getId)
                .collect(Collectors.toList());

        return new DirectoryDTO(
                directory.getId(),
                directory.getName(),
                directory.getPath(),
                directory.getParent() != null ? directory.getParent().getId() : null,
                childrenIds
        );
    }


    @Transactional
    public void deleteDirectory(UUID id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com o ID: " + id));

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

        return Map.of("Diretórios", totalDirectories, "Arquivos", totalFiles);
    }

    public long getFileCountInDirectory(UUID id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Diretório não encontrado com o ID: " + id));

        return jpaFileRepository.getAllFiles().stream()
                .filter(file -> file.getDirectory().equals(directory))
                .count();
    }

    public Map<UUID, Long> getTotalFileSizeByDirectory() {
        Map<UUID, Long> totalSizeByDirectory = new HashMap<>();

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

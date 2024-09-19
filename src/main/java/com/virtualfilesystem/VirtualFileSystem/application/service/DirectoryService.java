package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;

    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Transactional
    public void saveDirectory(Directory directory) {

        if(directory.getParent() != null){
            Directory parent = directoryRepository.getDirectoryByPath(directory.getParent().getPath());
            directory.setParent(parent);
        }

        saveOrUpdateDirectory(directory);
    }

    public void saveOrUpdateDirectory(Directory directory) {
        directoryRepository.saveDirectory(directory);

        for(Directory child : directory.getChildren()){
            child.setParent(directory);
            saveOrUpdateDirectory(child); // Recursividade
        }
    }

    public List<Directory> getAllDirectories(){
        return directoryRepository.getAllDirectories();
    }

    public Directory getDirectoryByPath(String path){
        return directoryRepository.getDirectoryByPath(path);
    }
}

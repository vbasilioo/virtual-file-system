package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void saveFile(File file){
        fileRepository.saveFile(file);
    }

    public List<File> getAllFiles(){
        return fileRepository.getAllFiles();
    }

    public File getFileByPath(String path){
        return fileRepository.getFileByPath(path);
    }
}

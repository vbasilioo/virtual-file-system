package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Configuration;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.ConfigurationRepository;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.FileRepository;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    DirectoryRepository directoryRepository;
    @Autowired
    ConfigurationRepository configurationRepository;

    public File saveFile(File file) {
        if(file.getDirectory() == null || directoryRepository.findById(file.getDirectory().getId()).isEmpty())
            throw new ApiException("Diretório pai não existe.", HttpStatus.BAD_REQUEST);

        Configuration config = configurationRepository.findById(UUID.fromString(UUID.randomUUID().toString()))
                .orElseThrow(() -> new ApiException("Configuração não encontrada", HttpStatus.INTERNAL_SERVER_ERROR));

        if(!config.hasEnoughMemory(file.getSize()))
            throw new ApiException("Espaço insuficiente para salvar o arquivo.", HttpStatus.BAD_REQUEST);

        File savedFile = fileRepository.saveFile(file);

        config.increaseUsedMemory(file.getSize());
        configurationRepository.save(config);

        return savedFile;
    }

    public void deleteFilesByDirectory(Directory directory) {
        List<File> files = fileRepository.getAllFiles().stream()
                .filter(file -> file.getDirectory().equals(directory))
                .toList();

        for (File file : files)
            fileRepository.delete(file);
    }

    public List<File> getAllFiles() {
        return fileRepository.getAllFiles();
    }

    public File getFileByPath(String path) {
        return fileRepository.getFileByPath(path);
    }

    private String getFileExtension(String fileName){
        if(fileName.lastIndexOf('.') == -1)
            return "";

        return fileName.substring(fileName.lastIndexOf('.')+1);
    }

    public Map<String, Long> getFileCountByExtension(){
        return getAllFiles().stream()
                .collect(Collectors.groupingBy(
                        file -> getFileExtension(file.getName()),
                        Collectors.counting()
                ));
    }
}

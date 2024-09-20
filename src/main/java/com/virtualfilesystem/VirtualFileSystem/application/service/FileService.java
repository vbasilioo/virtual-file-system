package com.virtualfilesystem.VirtualFileSystem.application.service;

import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.FileRepository;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.DirectoryRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final DirectoryRepository directoryRepository;

    public FileService(FileRepository fileRepository, DirectoryRepository directoryRepository) {
        this.fileRepository = fileRepository;
        this.directoryRepository = directoryRepository;
    }

    public File saveFile(File file) {
        if (file.getDirectory() == null || directoryRepository.findById(file.getDirectory().getId()).isEmpty())
            throw new ApiException("Diretório pai não existe.", HttpStatus.BAD_REQUEST);

        return fileRepository.saveFile(file);
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
}

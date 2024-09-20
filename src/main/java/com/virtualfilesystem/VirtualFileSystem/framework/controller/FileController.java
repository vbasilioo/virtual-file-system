package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.FileService;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ReturnApi saveFile(@RequestBody File file) {
        try {
            File savedFile = fileService.saveFile(file);
            return ReturnApi.success(savedFile, "Arquivo salvo com sucesso.");
        } catch (ApiException ex) {
            throw ex;
        }
    }

    @GetMapping
    public ReturnApi getAllFiles() {
        List<File> files = fileService.getAllFiles();
        return ReturnApi.success(files, "Arquivos recuperados com sucesso.");
    }

    @GetMapping("/{path}")
    public ReturnApi getFileByPath(@PathVariable String path) {
        try {
            File file = fileService.getFileByPath(path);
            return ReturnApi.success(file, "Arquivo recuperado com sucesso.");
        } catch (ApiException ex) {
            throw ex;
        }
    }
}

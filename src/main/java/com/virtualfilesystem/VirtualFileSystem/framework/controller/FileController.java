package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.FileService;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping
    public ReturnApi getAllFiles() {
        try {
            List<File> files = fileService.getAllFiles();
            return ReturnApi.success(files, "Arquivos recuperados com sucesso.");
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/{path}")
    public ReturnApi getFileByPath(@PathVariable String path) {
        try {
            File file = fileService.getFileByPath(path);
            return ReturnApi.success(file, "Arquivo recuperado com sucesso.");
        } catch (ApiException ex) {
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/statistics/files-by-extension")
    public ResponseEntity<ReturnApi> getFileCountByExtension() {
        try {
            Map<String, Long> fileCountByExtension = fileService.getFileCountByExtension();
            return ResponseEntity.ok(ReturnApi.success(fileCountByExtension, "Contagem de arquivos por extensão recuperada com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }
}

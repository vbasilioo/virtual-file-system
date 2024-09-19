package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.FileService;
import com.virtualfilesystem.VirtualFileSystem.domain.model.File;
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
    public void saveFile(@RequestBody File file){
        fileService.saveFile(file);
    }

    @GetMapping
    public List<File> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{path}")
    public File getFileByPath(@PathVariable String path){
        return fileService.getFileByPath(path);
    }
}

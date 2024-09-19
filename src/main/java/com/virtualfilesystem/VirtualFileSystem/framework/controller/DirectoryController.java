package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.DirectoryService;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directories")
public class DirectoryController {
    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostMapping
    public void saveDirectory(@RequestBody Directory directory) {
        directoryService.saveDirectory(directory);
    }

    @GetMapping
    public List<Directory> getAllDirectories() {
        return directoryService.getAllDirectories();
    }

    @GetMapping("/{path}")
    public Directory getDirectoryByPath(@PathVariable String path) {
        return directoryService.getDirectoryByPath(path);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectory(@PathVariable Long id) {
        directoryService.deleteDirectory(id);
    }
}

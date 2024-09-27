package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.DirectoryService;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/directories")
public class DirectoryController {
    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostMapping
    public ResponseEntity<ReturnApi> saveDirectory(@RequestBody Directory directory) {
        Directory directorySave = directoryService.saveDirectory(directory);
        return ResponseEntity.ok(ReturnApi.success(directorySave, "Diretório salvo com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ReturnApi> getAllDirectories() {
        List<Directory> directories = directoryService.getAllDirectories();
        return ResponseEntity.ok(ReturnApi.success(directories, "Todos os diretórios foram listados com sucesso."));
    }

    @GetMapping("/{path}")
    public ResponseEntity<ReturnApi> getDirectoryByPath(@PathVariable String path) {
        Directory directory = directoryService.getDirectoryByPath(path);

        if (directory == null)
            throw new ApiException("Diretório não encontrado para o caminho: " + path);

        return ResponseEntity.ok(ReturnApi.success(directory, "Diretório encontrado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReturnApi> deleteDirectory(@PathVariable Long id) {
        directoryService.deleteDirectory(id);
        return ResponseEntity.ok(ReturnApi.success(null, "Diretório excluído com sucesso."));
    }

    @GetMapping("/statistics/overview")
    public ResponseEntity<ReturnApi> getOverviewStatistics(){
        Map<String, Long> statistics = directoryService.getOverviewStatistics();
        return ResponseEntity.ok(ReturnApi.success(statistics, "Estatísticas gerais recuperadas com sucesso."));
    }

    @GetMapping("/statistics/file-count/{id}")
    public ResponseEntity<ReturnApi> getDirectoryStatistics(@PathVariable Long id) {
        long fileCount = directoryService.getFileCountInDirectory(id);
        return ResponseEntity.ok(ReturnApi.success(Map.of("directoryId", id, "fileCount", fileCount),
                "Contagem de arquivos no diretório recuperada com sucesso."));
    }

    @GetMapping("/statistics/total-file-size")
    public ResponseEntity<ReturnApi> getTotalFileSizeByDirectory() {
        Map<Long, Long> totalSizeByDirectory = directoryService.getTotalFileSizeByDirectory();
        return ResponseEntity.ok(ReturnApi.success(totalSizeByDirectory, "Tamanho total dos arquivos por diretório recuperado com sucesso."));
    }
}

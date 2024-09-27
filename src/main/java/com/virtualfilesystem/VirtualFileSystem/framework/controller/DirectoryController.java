package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.DirectoryService;
import com.virtualfilesystem.VirtualFileSystem.domain.DTO.Directory.DirectoryDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.model.User;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/directories")
public class DirectoryController {
    @Autowired
    DirectoryService directoryService;

    @PostMapping
    public ResponseEntity<ReturnApi> saveDirectory(@RequestBody DirectoryDTO directory) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            DirectoryDTO directorySaved = directoryService.saveDirectory(directory, user);
            return ResponseEntity.ok(ReturnApi.success(directorySaved, "Diretório salvo com sucesso."));
        } catch (ApiException ex) {
            throw new ApiException(ex.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<ReturnApi> getAllDirectories() {
        try {
            List<DirectoryDTO> directories = directoryService.getAllDirectories();
            return ResponseEntity.ok(ReturnApi.success(directories, "Todos os diretórios foram listados com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/{path}")
    public ResponseEntity<ReturnApi> getDirectoryByPath(@PathVariable String path) {
        try {
            DirectoryDTO directory = directoryService.getDirectoryByPath(path);
            return ResponseEntity.ok(ReturnApi.success(directory, "Diretório encontrado com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReturnApi> deleteDirectory(@PathVariable UUID id) {
        try {
            directoryService.deleteDirectory(id);
            return ResponseEntity.ok(ReturnApi.success(null, "Diretório excluído com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/statistics/overview")
    public ResponseEntity<ReturnApi> getOverviewStatistics() {
        try {
            Map<String, Long> statistics = directoryService.getOverviewStatistics();
            return ResponseEntity.ok(ReturnApi.success(statistics, "Estatísticas gerais recuperadas com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/statistics/file-count/{id}")
    public ResponseEntity<ReturnApi> getDirectoryStatistics(@PathVariable UUID id) {
        try {
            long fileCount = directoryService.getFileCountInDirectory(id);
            return ResponseEntity.ok(ReturnApi.success(Map.of("directoryId", id, "fileCount", fileCount), "Contagem de arquivos no diretório recuperada com sucesso."));
        } catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }

    @GetMapping("/statistics/total-file-size")
    public ResponseEntity<ReturnApi> getTotalFileSizeByDirectory() {
        try {
            Map<UUID, Long> totalSizeByDirectory = directoryService.getTotalFileSizeByDirectory();
            return ResponseEntity.ok(ReturnApi.success(totalSizeByDirectory, "Tamanho total dos arquivos por diretório recuperado com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }
}

package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.application.service.DirectoryService;
import com.virtualfilesystem.VirtualFileSystem.domain.DTO.Directory.DirectoryDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.model.Directory;
import com.virtualfilesystem.VirtualFileSystem.domain.model.User;
import com.virtualfilesystem.VirtualFileSystem.domain.model.UserRole;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectoryControllerTest {

    @InjectMocks
    private DirectoryController directoryController;

    @Mock
    private DirectoryService directoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDirectorySuccess() {
        User user = new User("testUser", "password", UserRole.USER);
        user.setId(UUID.randomUUID());

        DirectoryDTO parentDirectory = new DirectoryDTO(null, "parentDirectory", "/path/to", null, new ArrayList<>());
        DirectoryDTO directory = new DirectoryDTO(null, "directoryName", "/path/to/directory", null, new ArrayList<>());

        when(directoryService.saveDirectory(directory, user)).thenReturn(directory);

        ResponseEntity<ReturnApi> response = directoryController.saveDirectory(directory);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Diretório salvo com sucesso.", response.getBody().getMessage());
    }

    @Test
    void testGetAllDirectoriesSuccess() {
        DirectoryDTO dir1 = new DirectoryDTO(UUID.randomUUID(), "Dir1", "/path/to/Dir1", null, new ArrayList<>());
        DirectoryDTO dir2 = new DirectoryDTO(UUID.randomUUID(), "Dir2", "/path/to/Dir2", null, new ArrayList<>());

        when(directoryService.getAllDirectories()).thenReturn(Arrays.asList(dir1, dir2));

        ResponseEntity<ReturnApi> response = directoryController.getAllDirectories();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, ((List<DirectoryDTO>) response.getBody().getData()).size());
        assertEquals("Todos os diretórios foram listados com sucesso.", response.getBody().getMessage());
    }


    @Test
    void testGetAllDirectoriesEmpty() {
        when(directoryService.getAllDirectories()).thenReturn(Collections.emptyList());

        ResponseEntity<ReturnApi> response = directoryController.getAllDirectories();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((List<Directory>) response.getBody().getData()).isEmpty());
    }

    @Test
    void testGetDirectoryByPathSuccess() {
        String path = "/path/to/directory";

        DirectoryDTO directoryDTO = new DirectoryDTO(UUID.randomUUID(), "directoryName", path, null, new ArrayList<>());

        when(directoryService.getDirectoryByPath(path)).thenReturn(directoryDTO);

        ResponseEntity<ReturnApi> response = directoryController.getDirectoryByPath(path);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Diretório encontrado com sucesso.", response.getBody().getMessage());
        assertEquals(directoryDTO, response.getBody().getData());
    }


    @Test
    void testDeleteDirectorySuccess() {
        UUID id = UUID.randomUUID();
        doNothing().when(directoryService).deleteDirectory(id);

        ResponseEntity<ReturnApi> response = directoryController.deleteDirectory(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Diretório excluído com sucesso.", response.getBody().getMessage());
    }

    @Test
    void testGetOverviewStatisticsSuccess() {
        Map<String, Long> statistics = Map.of("totalDirectories", 5L);
        when(directoryService.getOverviewStatistics()).thenReturn(statistics);

        ResponseEntity<ReturnApi> response = directoryController.getOverviewStatistics();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(statistics, response.getBody().getData());
        assertEquals("Estatísticas gerais recuperadas com sucesso.", response.getBody().getMessage());
    }

    @Test
    void testGetDirectoryStatisticsSuccess() {
        UUID id = UUID.randomUUID();
        long fileCount = 10;
        when(directoryService.getFileCountInDirectory(id)).thenReturn(fileCount);

        ResponseEntity<ReturnApi> response = directoryController.getDirectoryStatistics(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fileCount, ((Map<String, Long>) response.getBody().getData()).get("fileCount"));
        assertEquals("Contagem de arquivos no diretório recuperada com sucesso.", response.getBody().getMessage());
    }

    @Test
    void testGetTotalFileSizeByDirectorySuccess() {
        Map<UUID, Long> totalSizeByDirectory = Map.of(UUID.randomUUID(), 1024L);

        when(directoryService.getTotalFileSizeByDirectory()).thenReturn(totalSizeByDirectory);

        ResponseEntity<ReturnApi> response = directoryController.getTotalFileSizeByDirectory();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(totalSizeByDirectory, response.getBody().getData());
        assertEquals("Tamanho total dos arquivos por diretório recuperado com sucesso.", response.getBody().getMessage());
    }

}

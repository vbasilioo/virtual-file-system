package com.virtualfilesystem.VirtualFileSystem.domain.DTO.Directory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DirectoryDTO(UUID id, String name, String path, UUID parentId, List<DirectoryDTO> children) {
    public List<DirectoryDTO> children() {
        return children != null ? children : new ArrayList<>();
    }
}

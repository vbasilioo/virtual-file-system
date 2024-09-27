package com.virtualfilesystem.VirtualFileSystem.domain.DTO.Directory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DirectoryDTO(UUID id, String name, String path, UUID parentId, List<UUID> childrenIds) {
    public List<UUID> childrenIds() {
        return childrenIds != null ? childrenIds : new ArrayList<>();
    }
}


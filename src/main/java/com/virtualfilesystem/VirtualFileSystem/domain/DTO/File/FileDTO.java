package com.virtualfilesystem.VirtualFileSystem.domain.DTO.File;

import java.util.UUID;

public record FileDTO(UUID id, String name, String path, long size, UUID directoryId) {
}

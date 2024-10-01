package com.virtualfilesystem.VirtualFileSystem.domain.DTO.User;

import com.virtualfilesystem.VirtualFileSystem.domain.model.User;

public record LoginResponseDTO(String token, User user) {
}

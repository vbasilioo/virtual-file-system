package com.virtualfilesystem.VirtualFileSystem.domain.DTO.User;

import com.virtualfilesystem.VirtualFileSystem.domain.model.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {
}

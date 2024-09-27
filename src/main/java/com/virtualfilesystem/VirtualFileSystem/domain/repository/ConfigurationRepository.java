package com.virtualfilesystem.VirtualFileSystem.domain.repository;

import com.virtualfilesystem.VirtualFileSystem.domain.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
}

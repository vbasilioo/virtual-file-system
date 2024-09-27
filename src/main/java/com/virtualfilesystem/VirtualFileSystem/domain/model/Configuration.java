package com.virtualfilesystem.VirtualFileSystem.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "configurations")
@Data
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long totalMemory;
    private long usedMemory = 0;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Configuration(){}

    public Configuration(User user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseUsedMemory(long size) {
        this.usedMemory += size;
    }

    public boolean hasEnoughMemory(long size) {
        return (this.totalMemory - this.usedMemory) >= size;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

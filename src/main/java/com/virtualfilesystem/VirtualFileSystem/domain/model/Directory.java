package com.virtualfilesystem.VirtualFileSystem.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "directories")
@Data
@SQLDelete(sql = "UPDATE directories SET deleted_at = true WHERE id = ?")
@Where(clause = "deleted_at = false")
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String path;
    private String name;

    @Column(name = "deleted_at")
    private boolean deleted = false;

    @ManyToOne
    @JsonIgnore
    private Directory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Directory> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Directory() {}

    public Directory(String path, String name, User user) {
        this.path = path;
        this.name = name;
        this.user = user != null ? user : null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public List<Directory> getChildren() {
        return children;
    }

    public void setChildren(List<Directory> children) {
        this.children = children;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

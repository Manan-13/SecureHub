package com.petproject.Securehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class FileMetadata {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;
    private long size;
    private String path;
    @ManyToOne
    private User owner;
    private Instant timestamp;
}


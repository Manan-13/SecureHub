package com.petproject.Securehub.controller;

import com.petproject.Securehub.constant.ApplicationConstant;
import com.petproject.Securehub.entity.FileMetadata;
import com.petproject.Securehub.service.FileService;
import com.petproject.Securehub.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.petproject.Securehub.constant.ApplicationConstant.UPLOAD_DIR;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(fileService.saveFile(file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {

        FileMetadata meta = fileService.getFileIfOwner(id);

        File file = new File(UPLOAD_DIR, meta.getFilename());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + meta.getFilename() + "\"")
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<FileMetadata>> listFiles() {
        return ResponseEntity.ok(fileService.listUserFiles());
    }
}

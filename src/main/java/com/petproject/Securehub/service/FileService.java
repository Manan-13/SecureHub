package com.petproject.Securehub.service;

import com.petproject.Securehub.constant.ApplicationConstant;
import com.petproject.Securehub.entity.FileMetadata;
import com.petproject.Securehub.entity.User;
import com.petproject.Securehub.repository.FileMetadataRepository;
import com.petproject.Securehub.repository.UserRepository;
import com.petproject.Securehub.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.petproject.Securehub.constant.ApplicationConstant.UPLOAD_DIR;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final UserRepository userRepository;


    public FileMetadata getFileIfOwner(Long fileId) {
        String username = CommonUtils.getCurrentUser();
        FileMetadata meta = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!meta.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return meta;
    }

    public String saveFile(MultipartFile file){
        String username = CommonUtils.getCurrentUser();
        User user = userRepository.findByUsername(username).orElseThrow();

        File targetFile = new File(UPLOAD_DIR, file.getOriginalFilename());
        targetFile.getParentFile().mkdirs();
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileMetadata meta = new FileMetadata();
        meta.setFilename(file.getOriginalFilename());
        meta.setSize(file.getSize());
        meta.setTimestamp(Instant.now());
        meta.setOwner(user);

        fileMetadataRepository.save(meta);
        return "File Uploaded Successfully !";
    }

    public List<FileMetadata> listUserFiles() {
        String username = CommonUtils.getCurrentUser();
        return fileMetadataRepository.findByOwner_Username(username);
    }
}

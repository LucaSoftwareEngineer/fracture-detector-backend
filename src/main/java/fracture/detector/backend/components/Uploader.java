package fracture.detector.backend.components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Uploader {

    private final Path path;

    public String upload(MultipartFile fileBinary, String fileName) throws IOException {
        String fileUploaded = path.getCloudPath().concat(fileName);
        fileBinary.transferTo(new File(fileUploaded));
        return fileUploaded;
    }

}
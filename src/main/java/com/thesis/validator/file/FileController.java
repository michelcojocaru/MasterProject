package com.thesis.validator.file;

import com.thesis.validator.logic.Checker;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.SystemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadsDir;

    @PostMapping("/evaluateSystem")
    public Response uploadFile(@RequestBody SystemModel model) {
        try {

            CrystalGlobe crystalGlobe = new CrystalGlobe(model.services, model.relations, model.useCaseResponsibility, model.averageType, model.algorithms, model.repo);

            Checker checker = new Checker();
            checker.getFirstTest().runAssessment(crystalGlobe);

            return new Response(crystalGlobe.getResults());

        } catch (Exception e) {
            return new Response(e.toString());
        }
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

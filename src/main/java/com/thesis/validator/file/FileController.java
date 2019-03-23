package com.thesis.validator.file;
import com.thesis.validator.enums.Result;
import com.thesis.validator.logic.Checker;
import com.thesis.validator.model.SystemModel;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadsDir;

    private JSONObject loadJSON(String path){
        JSONObject jsonContent = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            jsonContent = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonContent;
    }

    @PostMapping("/evaluateSystem")
    public UploadFileResponse uploadFile(@RequestBody SystemModel model) {

        Result granularity = null;
        Result cohesion = null;
        Result coupling = null;

        try {
            granularity = Checker.calculateGranularity(model.services) ? Result.passed : Result.failed;
            cohesion = Checker.calculateCohesion(model.services,  model.relations) ? Result.passed : Result.failed;
            coupling = Checker.calculateCoupling(model.services,  model.relations) ? Result.passed : Result.failed;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new UploadFileResponse(granularity, cohesion, coupling);
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

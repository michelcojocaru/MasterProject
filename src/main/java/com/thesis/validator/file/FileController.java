package com.thesis.validator.file;
import com.thesis.validator.enums.Result;
import com.thesis.validator.logic.Checker;
import com.thesis.validator.model.SystemModel;
import org.json.JSONArray;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    //TODO rename methods into something like evaluateSystem
    @PostMapping("/evaluateSystem")
    public UploadFileResponse uploadFile(@RequestBody SystemModel model) {
        //String fileName = fileStorageService.storeFile(model);
        ArrayList<String> results = new ArrayList<>();

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();

        //JSONObject model = loadJSON(System.getProperty("user.dir") + uploadsDir.substring(1).concat("/") + fileName);
        try {
            results.add(Checker.calculateGranularity(model.services) ? Result.passed.name() : Result.failed.name()); //(JSONArray) model.get("services")) ? Result.passed.name() : Result.failed.name()
            results.add(Checker.calculateCoupling(model.services, model.relations) ? Result.passed.name() : Result.failed.name()); //(JSONArray) model.get("services"), (JSONArray) model.get("relations")) ? Result.passed.name() : Result.failed.name());
            results.add(Checker.calculateCohesion(model.services, model.relations) ? Result.passed.name() : Result.failed.name()); //(JSONArray) model.get("services"), (JSONArray) model.get("relations")) ? Result.passed.name() : Result.failed.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new UploadFileResponse(/*fileName, fileDownloadUri, file.getContentType(), file.getSize(),*/ results);
    }

//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

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

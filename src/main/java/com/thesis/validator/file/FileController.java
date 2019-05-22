package com.thesis.validator.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesis.validator.helpers.CSVOperations;
import com.thesis.validator.logic.Chain;
import com.thesis.validator.model.CrystalGlobe;
import com.thesis.validator.model.SystemModel;
import com.thesis.validator.tools.InlineCompiler;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

            CrystalGlobe crystalGlobe = new CrystalGlobe(model.name, model.services, model.relations, model.useCaseResponsibility, model.averageType, model.algorithms, model.repo);
            CSVOperations.initRunningTimesLogFile(model.name,"running_times", "csv");
            Chain chain = new Chain();
            chain.getFirstChecker().runAssessment(crystalGlobe);

            String fileName = CSVOperations.initRunningTimesLogFile(model.name,"scores", "json");

            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(fileName, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(fileWriter, crystalGlobe.getResults());
            } catch (Exception e) {
                e.printStackTrace();
            }

//            if(fileWriter != null){
//
//                try(PrintWriter writer = new PrintWriter(fileWriter)){
//
//
//                    writer.write(crystalGlobe.getResults().toString());
//
//                    //writer.close();
//
//                }
//            }

            return new Response(crystalGlobe.getResults());
            //TODO write results to files

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

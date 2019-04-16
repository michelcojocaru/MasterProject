package com.thesis.validator.file;

import com.thesis.validator.logic.Checker;
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
            InlineCompiler.run(crystalGlobe,"package com.thesis.validator.logic;\n" +
                    "\n" +
                    "import com.thesis.validator.enums.Averages;\n" +
                    "import com.thesis.validator.enums.Feedback;\n" +
                    "import com.thesis.validator.enums.SimilarityAlgorithms;\n" +
                    "import com.thesis.validator.enums.Tests;\n" +
                    "import com.thesis.validator.model.*;\n" +
                    "\n" +
                    "import java.text.DecimalFormat;\n" +
                    "import java.util.HashMap;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "public class NewAttributeChecker extends Attribute {\n" +
                    "\n" +
                    "    private static final double NEW_ATTRIBUTE_COEFFICIENT_OF_VARIATION_THRESHOLD = 0.0;\n" +
                    "    private Attribute chain;\n" +
                    "\n" +
                    "\n" +
                    "    public HashMap<String, TestResult> assessAttribute(List<Service> services,\n" +
                    "                                                List<Relation> relations,\n" +
                    "                                                UseCaseResponsibility useCaseResponsibilities,\n" +
                    "                                                Averages averageType,\n" +
                    "                                                List<SimilarityAlgorithms> algorithms,\n" +
                    "                                                Repo repo) {\n" +
                    "        HashMap<String,TestResult> resultScores = new HashMap<>();\n" +
                    "        TestResult testResult = null;\n" +
                    "        double result = 0.0;\n" +
                    "\n" +
                    "        /** Write here\n" +
                    "         * the implementation\n" +
                    "         * of your quality attribute\n" +
                    "         * assessment */\n" +
                    "\n" +
                    "        testResult = new TestResult(Tests.NEWATTRIBUTE_TEST, Double.parseDouble(new DecimalFormat(\".#\").format(result)));\n" +
                    "        Attribute.PopulateCauseAndTreatment(testResult,\n" +
                    "                Feedback.LOW_CAUSE_NEWATTRIBUTE.toString(),\n" +
                    "                Feedback.LOW_TREATMENT_NEWATTRIBUTE.toString(),\n" +
                    "                Feedback.MEDIUM_CAUSE_NEWATTRIBUTE.toString(),\n" +
                    "                Feedback.MEDIUM_TREATMENT_NEWATTRIBUTE.toString(),\n" +
                    "                Feedback.HIGH_CAUSE_NEWATTRIBUTE.toString(),\n" +
                    "                Feedback.HIGH_TREATMENT_NEWATTRIBUTE.toString());\n" +
                    "        resultScores.put(testResult.getTestName().name(),testResult);\n" +
                    "        return resultScores;\n" +
                    "    }\n" +
                    "}\n", checker);

            checker.getFirstChecker().runAssessment(crystalGlobe);

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

package com.thesis.validator.tools;


import com.thesis.validator.logic.Checker;
import com.thesis.validator.logic.Attribute;
import com.thesis.validator.model.CrystalGlobe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class InlineCompiler {

    private static InlineCompiler instance = null;
    private InlineCompiler() {}

    public static InlineCompiler getInstance() {
        if (instance == null) {
            instance = new InlineCompiler();
        }
        return instance;
    }

    public static void load(String userCode, Checker checker){
        File root = new File(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/"); // On Windows running on C:\, this is C:\java.
        File sourceFile = new File(root, "NewAttributeChecker.java");
        sourceFile.getParentFile().mkdirs();
        try {
            Files.write(sourceFile.toPath(), userCode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

// Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, System.out, null, sourceFile.getPath());
        while(!Files.exists(Paths.get(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/" + "NewAttributeChecker.class"))){
            System.out.println("Not found!");//this has to stay in here to delay the class search
        }
// Load and instantiate compiled class.
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Class<?> cls = null; // Should print "hello".
        try {
            cls = Class.forName("com.thesis.validator.logic.NewAttributeChecker", true, InlineCompiler.getInstance().getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object instance = null; // Should print "world".
        try {
            instance = cls.newInstance();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(instance);
        if (instance != null) {
            System.out.println("---- BOOOOOOOOOOOOM -----");
            Attribute userChecker = (Attribute) instance;
            //System.out.println(userChecker.pup());
            checker.registerAttributeChecker(userChecker);
        }
    }

    public static void run(CrystalGlobe crystalGlobe, String userCode, Checker checker) {

        Package pack = InlineCompiler.getInstance().getClass().getPackage();
        String packageName = pack.getName();

        StringBuilder sb = new StringBuilder(64000);
        sb.append(userCode);

//        sb.append("package "+ packageName +";\n");
//        //sb.append("package com.thesis.validator.tools;\n");
//        sb.append("public class UserAttributeChecker implements Attribute {\n");
//
//        sb.append("    public void calculateAttribute() {\n");
//        sb.append("        System.out.println(\"Hello world\");\n");
//        sb.append("    }\n");
//        sb.append("}\n");

        File userAttributeCheckerCode = new File(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/NewAttributeChecker.java");
        if (userAttributeCheckerCode.getParentFile().exists() || userAttributeCheckerCode.getParentFile().mkdirs()) {

            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(userAttributeCheckerCode);
                    writer.write(sb.toString());
                    writer.flush();
                } finally {
                    try {
                        Objects.requireNonNull(writer).close();
                    } catch (Exception e) {
                    }
                }

                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                // This sets up the class path that the compiler will use.
                // I've added the .jar file that contains the DoStuff interface within in it...
                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + ";dist/InlineCompiler.jar");

                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(userAttributeCheckerCode));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                if (task.call()) {
                    System.out.println("Yipe");
                    // Create a new custom class loader, pointing to the directory that contains the compiled
                    // classes, this should point to the top of the package structure!
                    //URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});

                    // Load the class from the classloader by name....
                    //Class<?> loadedClass = classLoader.loadClass("com.thesis.validator.logic.NewAttributeChecker");
                    // Create a new instance...
                    //Object obj = loadedClass.newInstance();
                    //TODO busy waiting... avoid this shit!
                    File javaFile = new File(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/" + "NewAttributeChecker" + ".java");
                    //boolean exists = classFile.exists();
                    while(!javaFile.exists() || !javaFile.isFile())
                    {
                        System.out.println("file exists, and it is a file");
                    }
                    Class cls = Class.forName("com.thesis.validator.logic.NewAttributeChecker");
                    Attribute obj = (Attribute) cls.newInstance();
                    //Object obj = Class.forName("com.thesis.validator.logic.NewAttributeChecker").newInstance();
//                    while(obj == null) {
//                        obj = Class.forName("com.thesis.validator.logic.NewAttributeChecker").newInstance();
//                    }

                    // Santity check
                    //if (obj instanceof Attribute) {
                    if (obj != null) {
                        System.out.println("---- BOOOOOOOOOOOOM -----");
                        // Cast to the DoStuff interface
                        Attribute userChecker = (Attribute)obj;
                        checker.registerAttributeChecker(userChecker);
                        // Run it baby
                        //userChecker.runAssessment(crystalGlobe);
                    }
//                    File codeFile = new File(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/NewAttributeChecker.java");
//                    if(codeFile.delete())
//                    {
//                        System.out.println("File deleted successfully");
//                    }
                    File classFile = new File(System.getProperty("user.dir") + "/src/main/java/com/thesis/validator/logic/NewAttributeChecker.class");

                    if(classFile.delete())
                    {
                        System.out.println("File deleted successfully");
                    }
                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();

            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
                exp.printStackTrace();
            }
        }
    }



}
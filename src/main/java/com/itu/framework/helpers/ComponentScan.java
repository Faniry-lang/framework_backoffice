package com.itu.framework.helpers;

import com.itu.framework.annotations.Controller;
import com.itu.framework.annotations.UrlMapping;
import com.itu.framework.annotations.GET;
import com.itu.framework.annotations.POST;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Version JAR-compatible du ComponentScan du framework externe.
 * Override uniquement la partie de scan des fichiers pour supporter les JARs.
 */
public class ComponentScan {

    public static Map<String, List<Mapping>> scanControllers(String packageName) throws Exception {
        Map<String, List<Mapping>> urlMappings = new HashMap<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);
        boolean foundAtLeastOne = false;

        while (resources.hasMoreElements()) {
            foundAtLeastOne = true;
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol)) {
                // Development mode - scan from filesystem
                try {
                    File dir = new File(resource.toURI());
                    scanFromDirectory(dir, packageName, urlMappings);
                } catch (Exception e) {
                    // If file scan fails, ignore (might be a JAR URL disguised as file)
                    System.err.println("Warning: Could not scan directory " + resource + ": " + e.getMessage());
                }
            } else if ("jar".equals(protocol)) {
                // Production mode - scan from JAR
                scanFromJar(resource, path, packageName, urlMappings);
            }
        }

        if (!foundAtLeastOne) {
            throw new IllegalArgumentException("Package " + packageName + " not found");
        }

        return urlMappings;
    }

    private static void scanFromDirectory(File dir, String packageName, Map<String, List<Mapping>> urlMappings) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                processClass(className, urlMappings);
            }
        }
    }

    private static void scanFromJar(URL resource, String path, String packageName, Map<String, List<Mapping>> urlMappings) throws Exception {
        JarURLConnection jarConn = (JarURLConnection) resource.openConnection();
        try (JarFile jarFile = jarConn.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();
            String prefix = path + "/";

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!name.startsWith(prefix) || entry.isDirectory()) continue;
                String rest = name.substring(prefix.length());
                if (!rest.endsWith(".class")) continue;
                if (rest.contains("/")) continue; // Skip sub-packages

                String className = packageName + "." + rest.substring(0, rest.length() - 6);
                processClass(className, urlMappings);
            }
        }
    }

    private static void processClass(String className, Map<String, List<Mapping>> urlMappings) {
        try {
            Class<?> cls = Class.forName(className);
            if (!cls.isAnnotationPresent(Controller.class)) return;

            Controller controller = cls.getAnnotation(Controller.class);
            String basePath = controller.value();

            // Process all methods in this controller
            Arrays.stream(cls.getDeclaredMethods()).forEach(method -> {
                if (!method.isAnnotationPresent(UrlMapping.class)) return;

                UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                String methodPath = urlMapping.value();
                String fullPath = (basePath + methodPath).replaceAll("/+", "/");

                // Determine HTTP method
                String httpMethod = "GET"; // default
                if (method.isAnnotationPresent(POST.class)) httpMethod = "POST";
                else if (method.isAnnotationPresent(GET.class)) httpMethod = "GET";

                // Add to mappings
                urlMappings.computeIfAbsent(fullPath, k -> new ArrayList<>());
                List<Mapping> mappings = urlMappings.get(fullPath);

                // Check for duplicate mappings
                for (Mapping existing : mappings) {
                    if (existing.getHttpMethod().equals(httpMethod)) {
                        throw new IllegalStateException("Duplicate mapping for " + fullPath + " " + httpMethod + " already mapped");
                    }
                }

                mappings.add(new Mapping(cls.getName(), method.getName(), httpMethod));
            });

        } catch (ClassNotFoundException e) {
            // Ignore classes that can't be loaded
            System.err.println("Warning: Could not load class " + className + ": " + e.getMessage());
        } catch (NoClassDefFoundError e) {
            // Ignore classes with missing dependencies
            System.err.println("Warning: Missing dependencies for class " + className + ": " + e.getMessage());
        }
    }
}

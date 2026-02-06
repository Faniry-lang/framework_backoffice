package itu.framework.backoffice.code_generator;

import legacy.utils.EntityGenerator;

public class EntityCodeGenerator {

    public static void main(String[] args) throws Exception {
        String outputFolderPath = "src/main/java";
        String packageName = "itu.framework.backoffice.entities";

        EntityGenerator.generateEntity("hotel", "TABLE", outputFolderPath, packageName);
        EntityGenerator.generateEntity("reservation", "TABLE", outputFolderPath, packageName);
    }
}


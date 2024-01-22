package org.xomda.core;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Constants {

    // the name of the Gradle Configuration
    // but also "src/[OMDA_CONFIGURATION]/java"
    String XOMDA = "xomda";
    String XOMDA_GROUP = "org.xomda";

    // .omda
    String DOT_XOMDA = "." + XOMDA;

    // The directories where to find the model CSV's
    Path XOMDA_DOT_PATH = Paths.get(DOT_XOMDA);
    Path XOMDA_CSV_CONFIG_PATH = Paths.get("src", XOMDA, "config");

    // The directories where to find OMDA Java code
    Path XOMDA_JAVA_PATH = Paths.get("src", XOMDA, "java");
    Path XOMDA_GENERATED_PATH = Paths.get("src", XOMDA, "generated");

}

package org.xomda.plugin.gradle;

public interface Constants extends org.xomda.core.Constants {

	String XOMDA_GRADLE_GROUP = "XOMDA";
	String XOMDA_PLUGIN_ID = XOMDA_GROUP + ".gradle";

	// the name of the Gradle Configuration
	// but also "src/[XOMDA_CONFIGURATION]/java"
	String XOMDA_CONFIGURATION = XOMDA;

	String XOMDA_SOURCESET_NAME = XOMDA;
	String XOMDA_GENERATED_SOURCE_NAME = "generated";

	// gradle tasks
	String XOMDA_TASK_COMPILE_TEMPLATES_NAME = "xomdaCompile";
	String XOMDA_TASK_COMPILE_TEMPLATES_DESC = "Compile the java XOMDA template code.";

	String XOMDA_TASK_GENERATE_TEMPLATE_NAME = "xomdaGenerate";
	String XOMDA_TASK_GENERATE_TEMPLATE_DESC = "Parse the models and execute the XOMDA templates.";

}

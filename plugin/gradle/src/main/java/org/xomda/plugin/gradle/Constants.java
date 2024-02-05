package org.xomda.plugin.gradle;

public interface Constants extends org.xomda.core.Constants {

	String XOMDA_PLUGIN_ID = XOMDA_GROUP + ".gradle";

	// the name of the Gradle Configuration
	// but also "src/[XOMDA_CONFIGURATION]/java"
	String XOMDA_CONFIGURATION = XOMDA;

	// gradle tasks
	String XOMDA_TASK_GENERATE_TEMPLATE_NAME = "generateXOmdaTemplates";
	String XOMDA_TASK_COMPILE_TEMPLATES = "compileXOmdaTemplates";

	String XOMDA_TASK_GENERATE_TEMPLATE_DESC = "Generate XOMDA templates.";

}

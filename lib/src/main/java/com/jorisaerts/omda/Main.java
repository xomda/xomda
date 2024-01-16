package com.jorisaerts.omda;

import com.jorisaerts.omda.csv.CsvService;
import com.jorisaerts.omda.logging.LoggerService;
import com.jorisaerts.omda.model.Package;
import com.jorisaerts.omda.template.TemplateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(final String[] args) throws IOException {
        LoggerService.init();

        final String csvFilename = ".omda/Model.csv";
        final Package pkg = new CsvService().read(".omda/Model.csv");

        logger.info("Found root: {}", pkg.getName());

        new TemplateService().generate(pkg);
    }

}

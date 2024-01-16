package com.jorisaerts.omda.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TemplateService {

    Logger logger = LogManager.getLogger(TemplateService.class);

    public <T> void generate(final T object) {
        logger.info(object);
    }

}

package org.xomda.core.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateContext {

    private final String outDir;

    public TemplateContext(String outDir) {
        this.outDir = outDir;
    }

    private final Map<Object, Object> userData = new ConcurrentHashMap<>();

    public Map<Object, Object> userData() {
        return userData;
    }

    public String outDir() {
        return this.outDir;
    }

    public static TemplateContext defaultContext() {
        return new TemplateContext(System.getProperty("user.dir"));
    }

}

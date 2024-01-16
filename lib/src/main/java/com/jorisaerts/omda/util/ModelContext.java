package com.jorisaerts.omda.util;

public class ModelContext {

    public ModelContext() {
        //final Package pkg = getClass().getClassLoader().getDefinedPackage("com.jorisaerts.omda.model");
    }

    private String[] classpath = {"com.jorisaerts.omda.model"};

    public String[] getClasspath() {
        return classpath;
    }

    public void setClasspath(final String[] classpath) {
        this.classpath = classpath;
    }

    public void setClasspath(final String classpath) {
        this.classpath = new String[]{classpath};
    }


}

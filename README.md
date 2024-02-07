# XOMDA

## Introduction

XOMDA stands for E**x**tensible **O**bject **M**odel **D**ata **A**bstraction.
It is used to generate code (such as interfaces, beans, dialogs, ...)
or any other asset from a parsed object model.

By generating code out of an object model, your whole application becomes predictable.
It's less error-prone to changes on any of the objects within your model.

### The Object Model

The object model is the definition of the different (configuration) objects
within your application. For example a User, a Product or an Order and all their respective properties.

An object model is typically defined in CSV format, because CSV easily to edit in tools like LibreOffice or Excel.  
Other formats are being considered to be implemented later on.

## Build and publishing

You can clean and build the project using:

```bash
./gradlew clean build
```

To publish the project to your local maven repository, use:

```bash
./gradlew publishToMavenLocal
```

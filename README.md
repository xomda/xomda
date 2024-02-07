# XOMDA

## Introduction

XOMDA stands for E**x**tensible **O**bject **M**odel **D**ata **A**bstraction.
It is used to generate code (such as interfaces, beans, dialogs, ...)
or any other asset from a parsed object model.

By generating code out of an object model, your whole application becomes predictable.
It will be less error-prone when you change or add properties to any of these object..

### The Object Model

An object model is typically defined in CSV format, because it's easily to manipulate.
Other formats are being considered to be implemented later on.

The object model is the definition of the different (configuration) objects
within your application. For example a User, a Product or an Order and all their respective properties.

## Build and publishing

You can clean and build the project using:

```bash
./gradlew clean build
```

To publish the project to your local maven repository, use:

```bash
./gradlew publishToMavenLocal
```

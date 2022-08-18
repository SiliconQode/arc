# ActiveJDBC Plugin

This project provides the ActiveJDBC gradle plugin. Although, this is a fork of the existing project, there is a small
error when using the recent version of the plugin in recent versions of gradle.

## Installation

From the command line in the root of this module execute the following command:

```bash
$ gradle clean build publishToMavenLocal
```

This will build the plugin and install it to your local maven repository. You can then use this in the rest of the
project

## Usage

Once you have compiled and installed the plugin into your local maven repository you can include it in any gradle
project as follows (assuming a recent version of gradle with the `plugins` section in the `build.gradle` file)

```groovy
plugins {
    id "dev.siliconcode.activejdbc-gradle-plugin" version "3.0-j11"
}
```

Once included into your build, it provides the following tasks:

* `instrumentModels` - This instruments compiled class files extending from `org.javalite.activejdbc.Model`. Essentially
  it provides the ORM functionality. During compilation this adds a required file, `activejdbc_models.properties` to the
  final jar. This file lists all the model classes.
* `instrumentGroovyModels` - Similar as the previous task, except it additional processing for groovy generated classes.

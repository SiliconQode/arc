# MSUSEL LoC Metrics

## Introduction
This module provides a language independent method of collecting LoC measures for a variety of LoC metrics.

Currently we include the following LoC counts:

* SLOC - Source Lines of Code
* BLOC - Blank Lines of Code
* CLOC - Comment Lines of Code
* CSLOC - Comment and Source Lines of Code

Although the code is written in a language independent way, it does require the construction of profiles for
each language it can support. Currently the following languages are included in the default set of profiles:

* Java
* C#
* C++
* C
* Bourne Shell
* Ruby

## Goals
The main goal is to provide the capability to analyze the following metrics:

* SLOC - Source Lines of Code
* BLOC - Blank Lines of Code
* CLOC - Comment Lines of Code
* CSLOC - Comment and Source Lines of Code
* DCLOC - Documentation Comment Lines of Code
* HCLOC - Header Comment Lines of Code
* HCWORD - Header Comment Words
* SLOC-L - Source Lines of Code - Executable Logical
* SLOC-P - Source Lines of Code - Executable Physical

For the top 100 programming languages as identified by the TIOBE and PYPL indexes.

The second goal is to provide not only the ability to analyze a single file or group of files,
but to simply analyze any provided text. We have met this goal.

<<<<<<< HEAD
## Installing Maven

This project uses the Maven wrapper so that you do not need to install maven manually.
The first time you go to build this project, simply execute the following command:

```
./mvnw clean install -Dmaven.test.skip=true
```
or for windows:
```
.\mvnw.cmd clean package -Dmaven.test.skip=true
```

## Building

This project can be built using the following command:

```
./mvnw clean package -Dmaven.test.skip=true
```

This project can be compile, tested, or packaged with the following commands:

```
./mvnw clean compile
./mvnw clean test
./mvnw clean package
```
=======
## MSUSEL Project dependencies
This project depends on the following other MSUSEL sub-projects:
1. [msusel-parent](https://github.com/MSUSEL/msusel-parent/)

## Building
There are two options:

1. You can use your own version of Maven and run the following commands at the command line, from the project root directory:
   * Compiling:
      ```bash
        $ mvn clean compile -Dmaven.test.skip=true
      ```
   * Packaging into a Jar with dependencies
      ```bash
        $ mvn clean package -Dmaven.test.skip=true
      ```
   * Packaging into a Jar and deploying to the [maven repo](https://github.com/MSUSEL/msusel-maven-repo):
      ```bash
        $ mvn clean deploy -Dmaven.test.skip=true
      ```

2. You can use the Maven wrapper which comes with the project:
   * On Mac and Linux:
      - Compiling:
      ```bash
        $ ./mvnw clean compile -Dmaven.test.skip=true
      ```
      - Packaging into a Jar with Dependencies:
      ```bash
        $ ./mvnw clean package -Dmaven.test.skip=true
      ```
      - Packaging into a Jar and deploying to the [maven repo](https://github.com/MSUSEL/msusel-maven-repo):
      ```bash
        $ ./mvnw clean deploy -Dmaven.test.skip=true
      ```
   * Windows:
      - Compiling:
      ```bash
        $ .\mvnw.cmd clean compile -Dmaven.test.skip=true
      ```
      - Packaging into a Jar with Dependencies:
      ```bash
        $ .\mvnw.cmd clean package -Dmaven.test.skip=true
      ```
      - Packaging into a Jar and deploying to the [maven repo](https://github.com/MSUSEL/msusel-maven-repo):
      ```bash
        $ .\mvnw.cmd clean deploy -Dmaven.test.skip=true
      ```

## License
As will all projects from MSUSEL this project is licensed under the MIT open source license. All source files associated with this project should have a copy of the license at the top of the file.

If a build fails due to license header issues, this can be remedied using the following command sequence at the command line:

- With an independently installed Maven system:
    * Linux, Mac, Windows:
    ```bash
     $ mvn license:format
    ```
- Using the Maven Wrapper:
    * Linux and Mac:
    ```bash
     $ ./mvnw license:format
    ```
    * Windows:
    ```bash
     $ .\mvnw.cmd license:format
    ```
>>>>>>> ed621532bf6d887f1872e8045a090e1fcf03eb7a

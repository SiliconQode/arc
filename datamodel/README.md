# SiliconCode Arc DataModel

This module provides the constructs necessary to provide a model of a software project. The basic
component is the `CodeNode` A code node is simply an abstraction of some artifact that occurs within
the software. The `CodeNode` is further refined into several subtypes. Containing the entire set of
code nodes is the `CodeTree`. The CodeTree is the data structure representing the structure of the Project
as a whole.

## Table of Contents

## Installation

## Usage

### Code Tree
The Code Tree provides the means to store the structure of a software project. It's root node, is the
highest-level parent project of a system.

### Code Node Types
Code Nodes have a set of associated properties including:

* Unique Qualified Identifier
* Name
* Mapping of Metric name to Measurement Value

Beyond this each type of CodeNode has their own properties.

#### Project Node
An abstraction representing the project. Each ProjectNode can contain a set of sub-projects, a set of
files, and a set of modules.

#### Namespace Node
An abstraction of a package or namespace. Each PackageNode is associated with a set of types defined
as a part of the package and a set of packages defined as sub-packages within this package.

#### File Node
An abstraction representing a file within the project (typically a source code/test code file). Each
file's qualified identifier is its key as defined by some arbitrary system, while its name is the absolute
path within the filesystem it resides in. Files can then define a set of Types which are defined within
the file.

#### Type Node
An abstraction of a type. Currently a type is based on the notion from the Object-Oriented paradigm and
can be either a Class or an Interface. Types can contain a set of Fields and a set of Methods. The unique
qualified name for a type is a combination of its `package.type` name, whereas its name is simply the type name.

#### Field Node
An abstraction of a type variable. Here a field's unique qualified name is a combination of the containing
type's qualified name plus the field's variable name separated by a '#'. A field has several properties including
its associated variable Type and whether or not it is a collection type (i.e., and array or other data structure).

#### Method Node
An abstraction of a type method. A method's unique qualified name is similar to a fields but contains a parenthesized
listing of the parameter types associated with the method. Each method contains the following: a set of parameters,
and a set of statements. A method also has a return type associated with it. Each parameter is simply a pair consisting of
a type and a name.

#### Statement Node
An abstraction of the statements making up the body of a method. A statement has a defined Statement Type
and its unique qualified name is a combination of that type and a separate long integer representing the
count of that statement type within the system.

### Building
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

### License Formatting
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

## Contributing

## Credits

## License

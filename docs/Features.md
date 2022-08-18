* Update all modules and all of their classes to use Log4j2 as the logging framework, with a logger in each class
* ~~Shift all modules into a single hierarchical gradle project with multiple subprojects~~
* ~~Shift all projects to be under a single package name scheme: com.empirilytics.arc.*~~
* Integrate Arc MLP into the framework
* Start creating a UI
  - Web - Vue.js W/Boostrap Vue
  - Local/Desktop - JavaFX (Test using FXTest)
* Integrate the Base Server into Arc
* Implement all pattern cues
* Implement Rot Injectors and Detectors
* Create the following tools
  - Quamoco Validation tool
  - Quamoco Calibration tool
  - Quamoco Model Building Tool
  - Injector Validation tool
  - Detector Validataion Tool
  - Cue Validation tool
  - Integrate all tools into UI
* Continue work on Plugin Framework (consider using JPF)
  - Shift Parsers
  - Shift Injectors
  - Shift Generators
    To all use the plugin Framework
* Expand to other languages
  - Java 17
  - CSharp
  - Ruby
  - C++
  - Python
  - JavaScript
  - Groovy
  - Swift
  - Lua
  - Rust
  - Scala
  - TypeScript
* Implement Pattern Detection (as originally designed)
* Implement Correction Strategies
* Implement Quality Models Using Quamoco framework
  - Columbus
  - SQALE
  - SQUALE
  - QATCH
  - PIQUE
  - SIG (expand beyond maintainability)
  - QMOOD
* Testing improvements
  - All Modules Unit Tested to > 90%
  - Create Integration Tests within Modules between classes
  - Create Integration Tests between Modules
  - Add Systems Tests
  - Add Acceptance Tests
* Refactoring
  - Identify Location for Refactorings (i.e., Code Smells)
  - Refactor the Code Base
* Updates
  - Update Codebase to Java 17+
  - Doc comments for all classes and packages
  - Use the Java Module System
* TD and Quality
  - Add additional TD Calculation Approaches
  - Improve to include TDM capabilities
  - Add Charting
* Add Control ability (similar to injection) to Pattern Generation
* Improve Performance Efficiency
  - Identify Bottlenecks
  - Improve speed and results: Goal parse and create datamodel of a 3 MLOC project in < 2 minutes
    - Construct a Benchmark set of projects to test against for all languages that can be processed
  - Look into Project Quasar
  - Consider performance improvements to DB
* Datamodel
  - Add in the concept of the Snapshot for a project version
  - Add in Script concept for languages such as Groovy, Scala, JavaScript, Python, and Ruby
* Add in better capabilities for mining software repositories
* Revive the ability to add environments during parsing to speed up identification of unknown types
  - Consider how we might lookup this info using build/dependency managment capabilities

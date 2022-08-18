# SiliconCode Qatch Analyzers Module

This module contains the classes used to execute language analysis tools, collect their results, and aggregate the
results into the model.

### Testing

Currently, testing is undefined for this module...

### Contributing

When adding a new language you need to do three things:

1. Create a new package named `com.empirilytics.qatch.analyzers.<language>`
2. Inside this new package create the following
    1. Create a new language provider named: `<Language>Provider.java` that extends `LanguageProvider`
    2. For the selected Metrics Tool
        1. Create a `<Tool>Aggregator` implementing the interface `Aggregator` which aggregates results into the model
        2. Create a `<Tool>Analyzer` which executes the tool
        3. Create a `<Tool>Importer` which imports the results from the tool's output
    3. For the selected Findings Tool
        1. Create a `<Tool>Aggregator` implementing the interface `Aggregator` which aggregates results into the model
        2. Create a `<Tool>Analyzer` which executes the tool
        3. Create a `<Tool>Importer` which imports the results from the tool's output
3. Add The new `<Language>Provider.java` by fully qualified class name to the file `qatch.yml` in
   the `src/main/dist/conf` directory of the `cli:app` module.

# Authors and Acknowledgement

* Isaac D. Griffith, Ph.D. - Refactored and improved the original code making it extensible to other tools and
  languages. Additionally improved the code used to execute the tools, and removed most magic strings
* Miltiadis G. Siavvas, Ph.D. - Wrote the original code for the Java PMD and CKJM-ext Analyzers, Importers, and
  Aggregators

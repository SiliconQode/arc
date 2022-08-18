# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-06-19

### Changed

- Restructured the project to a multi-module gradle build
- Restructured the source file distribution into multiple modules
- Updated the License file to include Empirilytics
- The project README.md to improve the project documentation
- Refactored nearly all the existing code to include checks for null objects
- Moved the models, rulesets, etc. into a dist folder under the app/cli module in the source directory
- Removed all of the cloning (or prototype pattern based components) and replaced them with a flyweight based approach

### Added

- Gradle build files and updated dependencies in order to make the build faster and less dependent on a single machine's
  environment
- A microservice wrapper for the execution of the quality analysis
- Unit tests for the core module
- Documentation of the project

### Removed

- All of the Ant build components

### Fixed

[Unreleased]: https://github.com/empirilytics/qatch/compare/v2.0.0...HEAD

[0.0.1]: https://github.com/olivierlacan/keep-a-changelog/releases/tag/v2.0.0

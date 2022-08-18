# SiliconCode Qatch Core Module

This module contains the basic components of the QATCH Quality Evaluation system.

### Components

There are three key sections of this module:

* The `eval` package - This package contains all the components used in the evaluation of a quality model
* The `model` package - This package contains the components which make up the quality model itself
* The `util` package - This package contains only a single utility which reads and writes quality model files

### Testing

Currently, this module is united with the following coverage values:

* 95% of Classes are covered
* 94% of Methods are covered
* 93% of Lines are covered

### License

The original code is copyright (c) 2017 under the MIT License to ISSEL Soft Eng Team, while all new code is copyright (
c) 2022 under the MIT License to Empirilytics.

### Contributing

Currently, the core module is stable and code-complete.

### Authors and Acknowledgement

* **Isaac D. Griffith, Ph.D.** - Refactored the code, improved the model read/write approach, and improved the evaluation
  approach to allowing a single model instance to be used across multiple projects without worrying about cloning.
* **Miltiadis G. Siavvas, Ph.D.** - Author of the original code and originator of the QATCH approach.

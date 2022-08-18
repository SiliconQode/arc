# Qatch Calibration Module

The purpose of this module is to provide calibration of the models

* Follows the approach in the original paper
* Allows for threshold calibration for metrics
* Allows for model weight calibration based on AHP and fAHP of expert opinion

The original code for this was written in R, but will be transformed to Java/Groovy Code

### License

The original code is copyright (c) 2017 under the MIT License to ISSEL Soft Eng Team, while all new code is copyright (
c) 2022 under the MIT License to Empirilytics.

### Contributing

At this time this module has not been tested at all. However, the following is needed:

* The original paper suggest the use of the Analytical Hierarchy Process and the fuzzy variant thereof the generate the
  weights used by each characteristic for each property in the model.
    * Unfortunately, the original approach utilized `R` to process this, it would be best if we moved away from this and
      encapsulated this logic directly in either Java or Groovy using the Apache Commons Math libraries
    * This would remove the horrible hard-coded strings and paths riddling this module
* The code in this module also has some bindings to JavaFX, these need to be refactored out
* The code in this module is in serious need of refactoring
* The code in this module needs to be completely unit tested

### Authors and Acknowledgement

* Isaac D. Griffith, Ph.D. - Refactored the code. Specifically, removed the thresholding `R` script and replaced it with
  actual Java Code.
* Miltiadis G. Siavvas, Ph.D. - Original author of the code in this section
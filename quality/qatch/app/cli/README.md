# SiliconCode Qatch CLI Module

This module provides the command line interface for the QATCH quality evaluation system.

### Installing

Before installing it is wise to first build the project using the following command:

```bash
$ gradle clean build
```

This will produce two files in the `build/distributions` directory:

* `qatch-2.0.0.tar`
* `qatch-2.0.0.zip`

Both of these files contain the entire distribution. To install you simply need to extract this to the directory in
which you want to install. For example, on Linux, you can install this to the opt directory as follows:

```bash
$ unzip -d /opt/qatch ./qatch-2.0.0.zip
```

```bash
$ tar -xvf ./qatch-2.0.0.tar /opt/qatch
```

Once extracted you need to add the following environment variable that points to the directory extract (one containing
the `bin` directory)

```
QATCH_HOME=<path to qatch>
```

everything else should be correctly configured

I also suggest that you include `$QATCH_HOME/bin` as part of your `$PATH` for easy execution

### Usage

The cli contains many different options and flags that can be used to control its operations. For more information use

```bash
qatch -h # or --help to see the help display
```

### Testing

[See Below ...](#contributing)

### License

The original code is copyright (c) 2017 under the MIT License to ISSEL Soft Eng Team, while all new code is copyright (
c) 2022 under the MIT License to Empirilytics.

### Contributing

Currently, this module is code-complete, however the following is still needed:

* Development of Unit Tests (I suggest reviewing
  the [PicoCli Guide for Testing Your Application](https://picocli.info/#_testing_your_application))
* Development of Integration Tests
* Development of System/Acceptance Tests

### Distribution Components

In the folder `src/main/dist` are the additional distribution files:

* `conf` this directory contains the configuration information
* `models` this directory contains the current QATCH models
* `rulesets` this directory contains customized rulesets used for the tools
* `tools` this directory contains the tools used for evaluations

### Authors and Acknowledgement

This module was contributed to by:

* **Isaac D. Griffith, Ph.D.** - constructed the CLI, and extracted and revitalized the code based on the Single Project,
  Multi-Project, and Benchmark Runners developed by Miltiadis G. Siavvas.
* **Miltiadis G. Siavvas, Ph.D.** - original author of the QATCH system and developed the underlying project runners the CLI
  is based on

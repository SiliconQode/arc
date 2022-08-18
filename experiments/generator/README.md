# arc-experiment-generator

This project presents the SiliconCode Arc Framework Experiment Generator.

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
   1. [Configuration File](#configuration-file)
3. [Contributing](#contributing)
4. [Credits](#credits)
5. [License](#license)

## Installation

Execute the following command:

```bash
> gradlew installDist
````

Which will generate `exgen.tar` and `exgen.zip` files in `app/build/dist` you can copy and unpackage these as you see fit.

## Usage

Run the following command

```bash
> exgen -c <config-file> <output-dir>
```

This will execute the experimental generator using the provided configuration file and generate the needed directory
the provided output directory

### Configuration File

```groovy
machines = 1         // number of machines that will be used in the experiment
replications = 1     // the number of experiment replications to run
severityLevels = 1   // the number of grime severity levels to use
javaHOme = ""        // location of java
gradleHome = ""      // location of gradle
detectorHome = ""    // location of the detectors used in the experiment
patternTypes = []    // list of strings naming patterns to be generated
injectionTypes = []  // list of strings naming the disharmony types to be injected
```

## Contributing

This is a private repo to the ISUESE laboratory. If you are truly interested in contributing, then simply do your graduate
or post-graduate work in this lab.

## Credits

- [Isaac Griffith](https://github.com/grifisaa)

## License

The MIT License (MIT)

SiliconCode Arc Framework Experimental Generator
Copyright (c) 2017-2022 SiliconCode, LLC

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

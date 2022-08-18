# results-aggregator
Experimental Results Aggregator

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

Which will generate `resagg.tar` and `resagg.zip` files in `app/build/dist` you can copy and unpackage these as you see fit.

## Usage

Run the following command

```bash
> resagg -c <config-file> -o <output-file> <base-dir>
```

This will execute the experimental generator using the provided configuration file and processing results files redursively
found from the base directory and resulting in the combined output file.

### Configuration File

```groovy
headers =  []    // list of strings naming column headers
```

## Contributing

This is a private repo to the ISUESE laboratory. If you are truly interested in contributing, then simply do your graduate
or post-graduate work in this lab.

## Credits

- [Isaac Griffith](https://github.com/grifisaa)

## License

The MIT License (MIT)

ISUESE Arc Framework Results Aggregator
Copyright (c) 2017-2021 Idaho State University Empirical Software Engineering
Laboratory

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
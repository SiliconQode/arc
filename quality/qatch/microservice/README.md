# SiliconCode Qatch Microservice Module

### Installation

Before installing, it is wise to first build the project using the following command:

```bash
$ gradle clean build
```

This will produce two files in the `build/distributions` directory:

* `qservice-2.0.0.tar`
* `qservice-2.0.0.zip`

Both of these files contain the entire distribution. To install you simply need to extract this to the directory in
which you want to install. For example, on Linux, you can install this to the `opt` directory as follows:

```bash
$ unzip -d /opt/qservice ./qservice-2.0.0.zip
```

```bash
$ tar -xvf ./qservice-2.0.0.tar /opt/qservice
```

Once extracted you need to add the following environment variable that points to the directory extract (one containing
the `bin` directory)

```
QSERVICE_HOME=<path to the service>
```

everything else should be correctly configured

I also suggest that you include `$QSERVICE_HOME/bin` as part of your `$PATH` for easy execution

### Usage

The cli contains two options that can be used to control its operations. For more information use

* `-p <num>` or `--port <num>` which sets the port the service will oprate on, if left unset it defaults to `8000`
* `-h` or `--help` displays the help information

#### API Endpoints

The following API endpoints are defined for this service:

* `/api/status` - (GET)
    * 200: returns message "Good to Go!"
    * 400 or 500: Server not running
* `/api/evaluate` - (POST)
    * requires a JSON data package consisting of the contents of the project to be analyzed
* `/api/evaluation/{id}` - (GET)
    * retrieves either the status or the results of the analysis for the project with the provided `id`
* `/swagger-ui` - Swagger documentation site for the API
* `/redoc` - ReDoc documentation site for the API

### Testing

[See Below ...](#contributing)

### License

The code is copyright (c) 2022 under the MIT License to Isaac D. Griffith, Ph.D.

### Contributing

* Although a prototype is currently working, it needs to be tested at all levels
* A `Dockerfile` and `docker-compose-yml` files exist but have yet to be tested
* A script to compose and push the images has been created, but not tested yet
* The components used to generate the files and conduct the evaluation currently run in a synchronous fashion
    * Thus, the api call posting the data remains connected until the analysis completes.
    * Instead, this should be done in an asynchronous way, placing the file generation and evaluation execution code in
      its own thread.

### Authors and Acknowledgements

* **Isaac D. Griffith, Ph.D.** - Refactored the code, improved the model read/write approach, and improved the
  evaluation approach to allowing a single model instance to be used across multiple projects without worrying about
  cloning.

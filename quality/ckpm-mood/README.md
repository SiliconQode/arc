# CKPM-MOOD

Calculates the Chidamber and Kemerer (CK) and MOOD Metrics for Python

## Metrics Currently Implemented:

**Traditional**
1. McCabe Cyclomatric Complexity (CC)
2. LOC, SLOC, CLOC etc
3. Comment Percentage (CP)

**CK**
1. Weighted Method per Class (WMC)
2. Depth of Inheritance Tree (DIT)
3. Number of Children (NOC)
4. Coupling between Objects (CBO)
5. Lack of Cohesion of Methods (LCOM)
6. Improved Lack of Cohesion of Methods (LCOM3)

**MOOD**
1. Method Hiding Factor (MHF)
2. Attribute Hiding Factor (AHF)
3. Method Inheritance Factor (MIF)
4. Coupling Factor (COF)

**Others**
1. Parameter Length
2. Number of Method per Class
3. Average Method Complexity (AMC)
4. Inheritance Coupling (IC)
5. Coupling Between Methods (CBM)
6. Afferent Coupling (Ca)
7. Efferent Coupling (Ce)

## Installation

## Usage

## Testing

To test the project simply execute the following:

```bash
$ python -m unittest .
```

## License

Copyright (C) 2022 under the MIT License to Isaac D. Griffith and Empirilytics

## Contributing

## Authors and Acknowledgement

This project was contributed to by:

* **Isaac D. Griffith, Ph.D.** - Implemented the syntax tree visitor, CK Metrics, and the CLI. Revamped all project documentation
  and improved the overall project structure.
* **Mahir** - Implemented the raw loc metrics component and was the original author of the project from which this one was forked.

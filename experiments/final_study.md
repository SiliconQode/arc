# Pattern Extraction Process

**Input:** Configuration
       Set of Design Pattern Instances
**Output:** Folders of Analyzable Design Pattern Instances Separated by Pattern Type, and Instance Number

## Setup
1. Pattern Name <- Config.projName
2. Pattern Inst Base <- Config.instBase
3. Pattern Inst File <- Config.instFile

## Analysis
4. Execute Java AI on Project
5. Execute Java Parser on Project

## Pattern Extraction
6. Instances <- collect(Pattern Inst File)
7. for i in Instances do
   1. for each type identified in i do
      1. Find all types that connect to i via
         - Association (to/from)
         - Generalization (to/from)
         - Realization (to/from)
         - Usage (to/from)
         - add containing file to set Files
      2. create correct directory structure
   2. for all f in Files do
      1. copy f to the appropriate directory in new directory structure
   3. Add instance.xml file to directory structure

# Pattern Instance Analysis Process

## For the chain ending set of pattern instances

**Input:** study config
**Output:** results file

### Setup
1. Read in the configuration
2. InstancesBase <- config.instBase

### Analysis
3. Execute Java AI
4. Execute Java Parser

### Quality Analysis
5. Execute Basic Metrics
6. Run Maintainability Analysis
7. Run TD Analysis

### Report Generator
8. Produce results.csv

## For the chain starting set of pattern instances

**Input:** study config, instance grime diff config
**Output:** results file

### Setup
1. Read in the configuration
2. InstancesBase <- config.instBase
3. InstancesGrimeConfFile <- config.grimeConfigFileName

### Analysis
4. Execute Java AI
5. Execute Java Parser
6. Execute Grime Injector (No copy)

### Quality Analysis
7. Execute Basic Metrics
8. Run Maintainability Analysis
9. Run TD Analysis

### Report Generator
10. Produce results.csv

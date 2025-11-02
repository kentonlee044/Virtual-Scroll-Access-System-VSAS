# Virtual Scroll Access System (VSAS) 

> **Group A3-T10-G09** | SOFT2412 2025 S2

A command line based application to simulate a virtual library for Whiskers to upload and enhance their knowledge.
## Team Members

|Role | Name | SID |
|-----|-----|-----|
|Product Owner | **Juan David Lopez Arce** | 540153206|
|Scrum Master | **Alex Daitz** | 540718728|
|Developer | **Kenton Lee** | 540745333|
|Developer | **Kegan Hofmeyr** | 540754690|

## Installation & Setup
1. Clone repository

```
$ git clone https://github.sydney.edu.au/SOFT2412-COMP9412-2025s2/A3-T10-G09.git

$ cd A3-T10-G09
```

2. Build project
```
$ gradle build
```


## Building, Testing and Running the application

### Build
Compile and test the project
```
$ gradle build
```
---
### Run
Launch application:
```
$ gradle run
```
or for a cleaner CLI output
```
$ gradle run --console=plain
```
Follow prompts in CLI to continue with application.

---
### Test
Execute automated test cases:
```
$ gradle test
```
Generate code coverage report with:
```
gradle jacocoTestReport
```
Report can be found at build/reports/jacoco/index.html


## Quick Start
```
# Build project
$ gradle build 

# Run application 
$ gradle run   
# (or gradle run --console==plain)

# Execute automated test cases
$ gradle test  

# If you encounter problems trying to build
$ gradle clean 

```

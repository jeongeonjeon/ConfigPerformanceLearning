# manual.pdf

## Tool: Configuration Performance Prediction using Ridge Regression

This tool predicts system performance from configuration inputs using WEKA's Ridge Regression implementation.

---

## Project Directory Overview

- Java Source Code: `src/main/java/uk/ac/bham/configPerformance/`
- Input CSV Dataset(s): `src/datasets/`
- Output Prediction Results: `src/result/`

---

## Run Instructions

### Option 1: Maven-based Execution

1. Compile the project:
```bash
mvn clean compile
```

2. Run the main class:
```bash
mvn exec:java -Dexec.mainClass="uk.ac.bham.configPerformance.ConfPerfLearningMain"
```

### Option 2: Manual Execution

1. Download the required JAR file:
   - [commons-math3-3.6.1.jar](https://mvnrepository.com/artifact/org.apache.commons/commons-math3/3.6.1)

2. Save to: `lib/commons-math3-3.6.1.jar`

3. Compile the source code:
```bash
javac -cp lib/commons-math3-3.6.1.jar -d bin src/main/java/uk/ac/bham/configPerformance/*.java
```

4. Run the main program:
```bash
java -cp lib/commons-math3-3.6.1.jar:bin uk.ac.bham.configPerformance.ConfPerfLearningMain
```

> Use `;` instead of `:` for classpath on Windows.

---

## Input Format

- **File location:** `src/datasets/**/*.csv` (multiple training datasets may exist)
- **Format:** Comma-separated values (CSV)

---

## Output Format

- **File generated:** `src/result/results.csv`

---

## Main Class

To run the program manually or through Maven, use:
```
uk.ac.bham.configPerformance.ConfPerfLearningMain
```
This is the entry point for executing Ridge Regression on configuration data.

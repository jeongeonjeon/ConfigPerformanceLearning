# requirements.pdf

## System Requirements

- **Operating System:** Windows 10/11, macOS, or Linux
- **Java Version:** Java 17 or later
- **Build Tool:** Maven 3.8.0+

---

## Directory Structure

- `src/main/` — Java source code
- `src/datasets/` — Input datasets (multiple `.csv` files supported)
- `src/result/` — Output prediction result files
- `pom.xml` — Maven project configuration

---

## Dependencies

### 1. Maven Dependency (Auto-installed)

This project uses **WEKA (Waikato Environment for Knowledge Analysis)** for Ridge Regression:

```xml
<dependency>
  <groupId>nz.ac.waikato.cms.weka</groupId>
  <artifactId>weka-stable</artifactId>
  <version>3.8.6</version>
</dependency>
```

> Maven will automatically download this dependency upon running `mvn install`.

---

### 2. Manual Dependency (commons-math3)

This project also depends on **Apache Commons Math 3.6.1**, which requires manual installation.

#### Step-by-Step:

1. Download the JAR file:  
   https://mvnrepository.com/artifact/org.apache.commons/commons-math3/3.6.1

2. Place it in a directory named `lib/` at the root of the project:

```
ConfigPerformanceLearning/
  |- lib/
      |- commons-math3-3.6.1.jar
```

3. Compile the project manually:

```bash
javac -cp lib/commons-math3-3.6.1.jar -d bin src/main/java/uk/ac/bham/configPerformance/*.java
```

4. Run the program manually:

```bash
java -cp lib/commons-math3-3.6.1.jar:bin uk.ac.bham.configPerformance.ConfPerfLearningMain
```

> On Windows, use `;` instead of `:` in the classpath.

---

## Notes

- All required datasets should be stored in: `src/datasets/`
- Output prediction files are generated in: `src/result/`
- Main executable class:
  ```
  uk.ac.bham.configPerformance.ConfPerfLearningMain
  ```



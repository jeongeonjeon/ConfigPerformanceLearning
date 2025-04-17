# replication.pdf

## Replicating Configuration Performance Learning Results

This guide outlines the steps to replicate predictions using Ridge Regression.

---

## 1. Clone the Repository

```bash
git clone https://github.com/jeongeonjeon/ConfigPerformanceLearning.git
cd ConfigPerformanceLearning
```

---

## 2. Project Structure

- `src/main/java/uk/ac/bham/configPerformance/ConfPerfLearningMain.java`
- `src/datasets/**/*.csv`
- `src/result/results.csv`

---

## 3. Build & Run

### Using Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="uk.ac.bham.configPerformance.ConfPerfLearningMain"
```

### Manual (requires `commons-math3-3.6.1.jar`)
See **`manual.md`** for detailed compilation and execution steps.

---

## 4. Output Verification

- Input: `src/datasets/**/*.csv`
- Output: `src/result/results.csv`

---

## 5. Contact

If you encounter issues:
- Email - jxj455@student.bham.ac.uk
- Git - https://github.com/jeongeonjeon/ConfigPerformanceLearning


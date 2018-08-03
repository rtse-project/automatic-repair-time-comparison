# How to COMPILE
In order to package the project call (it requires mongodb running with default settings)
```bash
mvn clean package
```

If you'd like to skip the test with:
```bash
mvn clean package -DskipTests
```

# How to USE
After the packaging, the jar can be found in the ./usage/target/ folder. Run the command:
```bash
java -cp usage-0.6.1.jar Main {name} {path}
```
Where:
 * `{name}` is a name for the project to analyze
 * `{path}` is the root path of the project
 
 Once the tool terminates, a file called `error.log` will be generated, along with the patches for the classes that contains problematic time comparisons.
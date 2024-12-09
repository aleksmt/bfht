# BFHT Testing Project (Kotlin-based)

This project is a Kotlin-based testing framework designed to perform two types of tests:
- **API Tests**: Functional tests to verify REST API functionality.
- **Load Testing**: Performance tests using Gatling to evaluate the system under specified load conditions.

## Prerequisites

- **Java 17 or higher**: Ensure your system is running Java version 17 or newer.
    ```shell
    java -version
    ```
- **Gradle**: Installed on your system. Alternatively, use the Gradle Wrapper (`./gradlew`).

## Configuration

Before running the tests, you need to configure the project by following these steps:

1. Locate the `application.example.conf` file in the `resources` directory.
2. Copy the file to `application.conf`:
    ```shell
    cp src/main/resources/application.example.conf src/main/resources/application.conf
    ```
3. Update the following keys in `application.conf` to match your deployed environment:
  - `service.restApiUrl`: The base URL of your REST API (e.g., `http://localhost:8080` for local Docker).
  - `service.wsHost`: The WebSocket host (e.g., `http://localhost:8080` for local Docker).
  - `service.auth.username`: The username for authentication.
  - `service.auth.password`: The password for authentication.

## Running Tests

### API Tests

To run API tests, execute the following command:
    ```shell
    ./gradlew test
    ```

The results will be available in the `build/test-results/test` directory. Open the `.xml` files or use a test reporting tool to view the results.

### Load Tests

The project uses Gatling for load testing. To execute load tests:

1. Ensure you have properly configured `application.conf`.
2. Run the following command:
    ```
    ./gradlew gatlingRun -Pgatling.simulationClass=TodoSimulation
    ```

### Viewing Results

After the Gatling load test completes, the results will be available in the `build/reports/gatling/<simulation-name>` directory. Open the `index.html` file in your browser to view a detailed report of the test results.


### Results with my hardware (VPS)

```text
VPS OS: Ubuntu 20.04 x64
1 CPU / 1024 Mb / 30 Gb / GEO: Russia (Moscow) <-> Georgia (Tbilisi)

================================================================================
---- Global Information --------------------------------------------------------
> request count                                       1650 (OK=1650   KO=0     )
> min response time                                    154 (OK=154    KO=-     )
> max response time                                    609 (OK=609    KO=-     )
> mean response time                                   177 (OK=177    KO=-     )
> std deviation                                         43 (OK=43     KO=-     )
> response time 50th percentile                        166 (OK=166    KO=-     )
> response time 75th percentile                        175 (OK=175    KO=-     )
> response time 95th percentile                        231 (OK=231    KO=-     )
> response time 99th percentile                        419 (OK=419    KO=-     )
> mean requests/sec                                  40.24 (OK=40.24  KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          1650 (   100%)
> 800 ms <= t < 1200 ms                                  0 (     0%)
> t >= 1200 ms                                           0 (     0%)
> failed                                                 0 (     0%)
================================================================================
```


## Example Scenarios

- **API Test Execution**:
  - Functional testing of API endpoints.
- **Load Testing**:
  - Evaluate the system under:
    - 10 requests per second (RPS).
    - 100 RPS.

## Additional Notes

- Ensure your system has access to the required Docker containers if you are using a local Docker environment.
- If running the application on a different host, update the configuration in `application.conf` accordingly.

Happy testing! 🚀

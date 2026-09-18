# Testing

Run `mvn clean verify`. JUnit 5 and Mockito exercise service create/defaults, lookup, update, status, deletion, missing IDs, and filter combinations. Standalone MockMvc checks HTTP codes, Location headers, bad input, error structure, and JSON output. These controller tests do not boot MySQL. The generated JaCoCo HTML report is in `target/site/jacoco/index.html`; inspect its actual figures before putting any percentage on a resume.

Manual API validation: start MySQL, run `mvn spring-boot:run`, then import `SmartTask.postman_collection.json` into Postman and run the create, list, status, and delete requests in order. Check that a second GET returns 404 after deletion. This manual check requires an active server and database and is distinct from automated unit tests.

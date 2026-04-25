## General Styling Guide

The name of the game is readability and [testability]. The following styling specifics reference Java, but are applicable to kotlin and groovy. 
- Packages should be all lowercase ASCII letters.
- Classes and Interfaces should follow a mixed case (aka "camel case"), with each subsequent word's first letter capitalized
    - e.g. `class TestClass`
- Follow [google's styleguide] for documentation. 
- Methods should be verb-based names, and should follow a mixed case (aka "camel case") pattern, with the first letter of the first word in lower case.
    - e.g. `void testMethod()`
    - Braces follow the Kernighan and Ritchie style ("Egyptian brackets") for nonempty blocks and block-like constructs:
    - Don't wrap a line just for the sake of not going past the 100 character marker. focus on readability.
    - Variables should have meaningful names, even in lambdas. There's no shortage of space, so write readable code whose names are indicative of their use.
      - Single character variable names or similar are not permitted. 
    - Example:
    ```java
    public String getReturnedResponse(Request request) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String returnedResponse;
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                returnedResponse = response.body().string();
            } else {
                throw new RuntimeException("Body of returned response is null.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return returnedResponse;
    }
    ``` 
    - Variables are similar to methods, in that they should follow a mixed case (aka "camel case") patter, with a
      lowercase first letter.

### Testing

- Tests should never run against production. 
- Use your development environment's auth token assigned to the `JUSTSERVE_TOKEN` environment variable in tests. 
- Methods and Features are to have [adequate] unit and integration tests written before any pull request can be accepted. 
- Because we use lombok, we don't need to test setters and getters. Using getters and setters is the preferred way to access class fields. 
- Unit test count is to scale appropriately according to the complexity of the method.
- Features are to have [adequate] integration and end-to-end tests.
- Fixes are to have [adequate] unit, integration and end-to-end tests included with the fix for the sake of [regression testing].
- Tests should only test one thing
    - e.g. `Set project owner.`
    - e.g. `Can NOT to set project owner with invalid UUID`
    - e.g. `Can update an Org description`
- Use data-driven testing to validate logic across all permutations of documented behavior like all `EventType` variants below.
    ```groovy
        @Unroll("can set contact info for #eventType.name() event")
        def "can set contact info for #eventType event"() {
            given:
            def event = baseEventBuilder()
                    .contactEmail(faker.internet().emailAddress())
                    .contactName(faker.name().fullName())
                    .contactPhone(faker.phoneNumber().phoneNumber())
                    .build()
            def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)
    
            when:
            client.createEvent(new CreateEventMutation(vars))
    
            then:
            noExceptionThrown()
    
            where:
            eventType << [EventType.DTL, EventType.Ongoing, EventType.MultipleDTL]
        }    
    ```

#### Adequate Testing Coverage
Adequate testing is determined by the method's documentation (this is why all methods require docs). Testing is surgical and specific; test exactly what is documented, no more and no less. If it's in the docs, then [test it]! The only exception to this is that [branches of code] should be covered in testing, which may not be documented.

[adequate]:#Adequate-Testing-Coverage
[branches of code]:https://medium.com/@zubairkhansh/branch-testing-and-branch-coverage-3fb4bbd9f949
[google's styleguide]:https://google.github.io/styleguide/javaguide.html#s7-javadoc
[regression testing]:https://www.browserstack.com/guide/regression-testing
[test it]:https://www.geeksforgeeks.org/software-engineering/difference-between-positive-testing-and-negative-testing/
[testability]:#Adequate-Testing-Coverage
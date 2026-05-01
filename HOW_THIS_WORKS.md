# How This Works
This is a basic explanation for contributors on how the OpenAPI generator builds the endpoints and why it is being used.

## How does the OpenAPI Generator Work?
The OpenAPI Generator is a tool that takes `.yml` files (in our case [schema.yml]) and uses them as a blueprint to generate code.

The structure of these files follow the [OpenAPI Specification (OAS)].

## Why Use the OpenAPI Generator?
Using the generator has advantages, such as:
* Saves time by automatically generating repetitive and verbose code
* Reduces the chance of human error in code implementation
* Ensures consistency across API specifications and implementation, especially for multiple contributors
* The generator can output to many different programming languages

[schema.yml]:src/main/resources/schema.yml

[OpenAPI Specification (OAS)]:https://swagger.io/docs/specification/v2_0/basic-structure/
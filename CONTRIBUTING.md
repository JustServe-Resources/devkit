# Contributing

By participating in this project, you agree to abide our
[code of conduct].

## Set up your machine

This project is written in [java] and [micronaut] using
Oracle's [GraalVM] and uses [gradle] to
handle dependencies.

Prerequisites:

- [Java 21 in graal's flavor Graal (community edition)]

clone the codebase, then cd into the repo and install the dependencies with gradle

```sh
git clone git@github.com:JustServe-Resources/devkit.git

./gradlew assemble
```

## Branch off of main

Don't make any changes to the main branch - these will be denied even if you try to. Branch naming conventions aren't enforced, though naming branches with a `my-last-name`/`task-name` convention isn't a bad idea.

Most importantly, see our [style guide](https://github.com/Graqr#general-styling-guide) for our coding standards. This project enforces [Conventional Commits], which is checked with each commit. 
## Test your change

Adequate acceptance testing is to be included with pull requests for new code. See our [style guide] for our testing standards. A portion of the codebase is generated during the build process. Using gradle's `build` task will both assemble and run tests. 

```sh
./gradlew build
```

## Validate this builds properly
This Micronaut application supports AOT (Ahead-of-Time) compilation to a GraalVM Native Image. This process produces a platform-specific binary with instant startup and lower memory overhead, though the compilation itself is resource-intensive. See [graal's docs] for optimization flags like quick build mode.
Running the test suite prior to the native build allows GraalVM to leverage profile-guided data for a more performant executable.
```sh
./gradlew :cli:test :cli:nativeCompile
```
> [!NOTE]
> This build may pass on your OS but may fail on another OS for which this cli compiles. These will be built and tested during PR checks 


## Submit a pull request

Push your branch to your `cli` fork and open a pull request against the original `cli` main branch. Contributions must pass all tests and CI before merging. Clearly showcase all changes and update [tests] accordingly. 

Please only mark the pull request as "Ready for Review" when CI tests are passing and the PR is in fact complete and ready for review.

[code of conduct]:CODE_OF_CONDUCT.md
[Conventional Commits]:https://www.conventionalcommits.org/en/v1.0.0/#summary
[GraalVM]:https://www.graalvm.org/
[graal's docs]:https://www.graalvm.org/latest/reference-manual/native-image/optimizations-and-performance/
[gradle]:https://docs.gradle.org/current/userguide/building_java_projects.html
[Java 21 in graal's flavor Graal (community edition)]:https://github.com/graalvm/graalvm-ce-builds/releases/
[micronaut]:https://micronaut.io/
[native executable]:https://www.graalvm.org/latest/reference-manual/native-image/guides/build-java-modules-into-native-executable/
[style guide]:STYLE_GUIDE.md#testing
[tests]:STYLE_GUIDE.md
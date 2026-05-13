# JustServe Resources

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=coverage)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=bugs)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=JustServe-Resources_cli&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=JustServe-Resources_cli)

This repository is architected as a modular, multi-module Micronaut project. At its center is the `core` module, a
reusable library providing a thoroughly tested HTTP client for the JustServe API.

Other modules leverage the `core` library to build specific applications. The `cli` module, for instance, is a GraalVM native command-line application that consumes the `core` client to provide administrative tooling.

As the project evolves, we adhere to semantic versioning. The API is subject to change, and any breaking modifications will be clearly communicated through major version increments.

## Cli Tool

### Install

<details><summary>instructions to set up graalvm build dependencies</summary>

You will need Visual Studio 2022 build tools installed on your machine to generate an executable with the graalvm, as well as GraalVM-CE v21
<ol>
<li>
Call this command to install both the visual studio community ide and its build tools. This also calls Chocolatey to install the graalvm, which you can choose to do through your IDE later as well.

```PowerShell
@("BuildTools", "Community" ) | 
    % { winget install "Microsoft.VisualStudio.2022.$($_)" }
    
choco install graalvm-java21 21.0.2
 ```
</li>
<li> After installing the Visual Studio Community IDE, install the "Desktop development with C++" package found under "Workloads".


> This can be found if you launch the IDE (select "continue without code"), then from the top menu select `Tools` -> `Get Tools and Features`. The installer will pop up with the workloads tab shown first.

</li>

<li> Be sure that `$env:java_home` is assigned to the graalvm.

```PowerShell
echo $env:java_home
```
</li>
</ol>
</details>

To generate the executable for your system, run `./gradlew :cli:nativeCompile`. The executable will be generated in the build directory (`cli/build/native/nativeCompile/`).

### Authenticate

Authenticate with this tool by defining the `JUSTSERVE_TOKEN` environment variable. Then request a token from the help center to populate the variable. 

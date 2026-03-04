import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

plugins {
    id("groovy")
    id("io.micronaut.application") version "4.6.2"
    id("com.gradleup.shadow") version "8.3.9"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "org.justserve"
version = project.properties["justserveCliVersion"]!!

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("info.picocli:picocli")
    implementation("info.picocli:picocli-jansi-graalvm:1.2.0")
    implementation("org.fusesource.jansi:jansi:2.4.2")
    implementation("info.picocli:picocli-shell-jline3:4.7.6")
    implementation("org.jline:jline:3.30.5")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("org.simplejavamail:simple-java-mail:8.12.6")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-http-client")
    implementation("org.simplejavamail:simple-java-mail:8.12.6")
    implementation("org.jsoup:jsoup:1.21.2")
    implementation(project(":core"))
    testImplementation("net.datafaker:datafaker:2.5.1")
    testImplementation("org.apache.commons:commons-lang3:3.20.0")
    testImplementation(project(path = ":core", configuration = "testArchives"))
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
}


application {
    mainClass = "org.justserve.JustServeCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

tasks.withType<ProcessResources> {
    val props = Properties()
    file("../gradle.properties").inputStream().use { props.load(it) }
    filesMatching("**/application.yml") {
        filter(mapOf("tokens" to props), ReplaceTokens::class.java)
    }
}

micronaut {
    testRuntime("spock2")
    processing {
        incremental(true)
        annotations("org.justserve.*")
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}

graalvmNative.binaries {
    named("main") {
        imageName.set("justserve")
        buildArgs.add("--color=always")
    }
}
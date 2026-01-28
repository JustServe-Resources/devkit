import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

plugins {
    id("groovy")
    id("io.micronaut.application") version "4.5.3"
    id("com.gradleup.shadow") version "8.3.6"
    id("io.micronaut.openapi") version "4.5.3"
    id("org.graalvm.buildtools.native") version "0.10.6"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

version = project.properties["justserveCliVersion"]!!
group = "org.justserve"

apply(from = "gradle/asciidoc.gradle")

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("info.picocli:picocli")
    implementation("info.picocli:picocli-jansi-graalvm:1.2.0")
    implementation("org.fusesource.jansi:jansi:2.4.2")
    implementation("info.picocli:picocli-shell-jline3:4.7.6")
    implementation("org.jline:jline:3.30.5")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-retry")
    implementation("org.simplejavamail:simple-java-mail:8.12.6")
    implementation("org.jsoup:jsoup:1.21.2")
    testImplementation("net.datafaker:datafaker:2.5.1")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
}


application {
    mainClass = "org.justserve.cli.JustServeCommand"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

tasks.withType<ProcessResources> {
    val props = Properties()
    file("gradle.properties").inputStream().use { props.load(it) }
    filesMatching("**/application.yml") {
        filter(mapOf("tokens" to props), ReplaceTokens::class.java)
    }
}

micronaut {
    testRuntime("spock2")
    openapi {
        version = "6.16.0"
        client(file("src/main/resources/schema.yml")) {
            apiPackageName = "org.justserve.client"
            modelPackageName = "org.justserve.model"
            useReactive = false
            useAuth = false
            lombok.set(true)
            clientId = "justserve"
            apiNameSuffix = "Client"
            alwaysUseGenerateHttpResponse = true
        }
    }
    processing {
        incremental(true)
        annotations("org.justserve.*")
    }
}

graalvmNative.binaries {
    named("main") {
        imageName.set("justserve")
        buildArgs.add("--color=always")
        buildArgs.add("-march=native")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}

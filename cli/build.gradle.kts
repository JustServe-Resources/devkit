import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

plugins {
    id("groovy")
    id("io.micronaut.application")
    id("com.gradleup.shadow")
    id("org.graalvm.buildtools.native")
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
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("info.picocli:picocli")
    implementation("info.picocli:picocli-jansi-graalvm:${project.properties["jansiGraalvmVersion"]}")
    implementation("org.fusesource.jansi:jansi:${project.properties["jansiVersion"]}")
    implementation("info.picocli:picocli-shell-jline3:${project.properties["picocliShellVersion"]}")
    implementation("org.jline:jline:${project.properties["jlineVersion"]}")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("org.simplejavamail:simple-java-mail:${project.properties["simpleJavaMailVersion"]}")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-http-client")
    implementation("org.jsoup:jsoup:${project.properties["jsoupVersion"]}")
    implementation(project(":core"))
    testImplementation("net.datafaker:datafaker:${project.properties["datafakerVersion"]}")
    testImplementation("org.apache.commons:commons-lang3:${project.properties["commonsLangVersion"]}")
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

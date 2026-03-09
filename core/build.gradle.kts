import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

plugins {
    id("groovy") 
    id("io.micronaut.library") version "4.5.3"
    id("io.micronaut.openapi") version "4.5.3"
    id("com.github.hauner.jarTest") version "1.1.0"
}

version = project.properties["justserveCliVersion"]!!
group = "org.justserve"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-retry")
    implementation("org.simplejavamail:simple-java-mail:8.12.6")
    implementation("org.jsoup:jsoup:1.21.2")
    compileOnly("io.micronaut:micronaut-http-client")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    testImplementation("net.datafaker:datafaker:2.5.1")
    testImplementation("org.apache.commons:commons-lang3:3.20.0")
    testImplementation("io.micronaut:micronaut-http-client")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

micronaut {
    testRuntime("spock2")
    openapi {
        version = "6.20.0"
        client(file("src/main/resources/schema.yml")) {
            apiPackageName = "org.justserve.client"
            modelPackageName = "org.justserve.model"
            useReactive = false
            useAuth = false
            lombok.set(true)
            clientId = "justserve"
            apiNameSuffix = "Client"
            alwaysUseGenerateHttpResponse = true
            additionalProperties.put("retryable", "true")
//            https://github.com/micronaut-projects/micronaut-openapi/discussions/1783
            schemaMapping.put("EventType", "org.justserve.model.EventType")
            importMapping.put("EventType", "org.justserve.model.EventType")
            schemaMapping.put("ProjectLocationType", "org.justserve.model.ProjectLocationType")
            importMapping.put("ProjectLocationType", "org.justserve.model.ProjectLocationType")
        }
    }
    processing {
        incremental(true)
        annotations("org.justserve.*")
    }
}

tasks.withType<ProcessResources> {
    val props = Properties()
    file("../gradle.properties").inputStream().use { props.load(it) }
    filesMatching("**/application.yml") {
        filter(mapOf("tokens" to props), ReplaceTokens::class.java)
    }
}
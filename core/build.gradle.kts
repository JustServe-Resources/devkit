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
    implementation("org.simplejavamail:simple-java-mail:${project.properties["simpleJavaMailVersion"]}")
    implementation("org.jsoup:jsoup:${project.properties["jsoupVersion"]}")
    implementation("io.micronaut.reactor:micronaut-reactor")
    compileOnly("io.micronaut:micronaut-http-client")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    testImplementation("net.datafaker:datafaker:${project.properties["datafakerVersion"]}")
    testImplementation("org.apache.commons:commons-lang3:${project.properties["commonsLang3Version"]}")
    testImplementation("io.micronaut:micronaut-http-client")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

micronaut {
    testRuntime("spock2")
    openapi {
        version = project.properties["micronautOpenapiSpecVersion"] as String
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
            schemaMapping.put("ProjectStatus", "org.justserve.model.ProjectStatus")
            importMapping.put("ProjectStatus", "org.justserve.model.ProjectStatus")
            schemaMapping.put("TimeZone", "org.justserve.model.TimeZone")
            importMapping.put("TimeZone", "org.justserve.model.TimeZone")
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
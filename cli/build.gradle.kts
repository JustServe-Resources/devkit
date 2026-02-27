import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

group = "org.justserve"
version = "0.0.8-SNAPSHOT"

tasks.withType<ProcessResources> {
    val props = Properties()
    file("gradle.properties").inputStream().use { props.load(it) }
    filesMatching("**/application.yml") {
        filter(mapOf("tokens" to props), ReplaceTokens::class.java)
    }
}
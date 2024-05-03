import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

fun Project.envOrProp(name: String): String{
    return env(name) ?: loadProperties().getProperty(name)
}

fun env(name: String, default: String? = null): String? = System.getenv(name) ?: default

fun Project.loadProperties(): Properties {
    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))
    return properties
}
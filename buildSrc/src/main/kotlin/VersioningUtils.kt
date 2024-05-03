import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

fun Project.envOrProp(name: String): String? = env(name) ?: loadProperties().getProperty(name)

fun env(name: String): String? = System.getenv(name)

fun Project.loadProperties(): Properties {
    val properties = Properties()

    val localPropertiesFile = rootProject.file("local.properties")

    if (!localPropertiesFile.exists()) return properties

    properties.load(FileInputStream(localPropertiesFile))

    return properties
}
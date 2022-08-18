package dev.siliconcode.qatch.service.lang.compilers

import dev.siliconcode.qatch.service.data.Project
import groovy.transform.NullCheck
import lombok.NonNull

/**
 * Class to generate a gradle build for a given project
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@NullCheck
class GradleProjectGenerator {

    /**
     * Sets up the directory structure for the gradle build
     *
     * @param basePath The base path for the project, cannot be null
     */
    void setupDirectoryStructure(@NonNull String basePath, @NonNull Project project, String[] dependencies) {
        println("Creating Directory Structure")
        File file = new File(basePath)
        file.deleteDir()
        FileTreeBuilder builder = new FileTreeBuilder(file)
        println("BasePath: " + basePath)
        builder {
            src {
                main {
                    java {

                    }
                }
                test {
                    java {

                    }
                }
            }
            'settings.gradle'("""\
            rootProject.name = '${project.name()}'
            """.stripIndent())
            'build.gradle'("""\
            plugins {
                id 'java'
            }

            repositories {
                mavenCentral()
            }

            dependencies {
                ${getDependencyListing(dependencies) ?: ""}

                ${getDefaultDependencies()}
            }
            """.stripIndent())
        }
    }

    /**
     * Converts the dependency list into a dependency listing for a gradle file
     * @param strings List of stirngs in standard gradle short form, cannot be null
     * @return A full dependency listing for a gradle build file
     */
    static String getDependencyListing(@NonNull String[] strings) {
        String builder = ""
        strings.each {
            builder += "    implementation " + "'${it}'" + "\n"
        }
        return builder
    }

    private String getDefaultDependencies() {
        return GradleProjectGenerator.class.getResourceAsStream("/standard_deps/java.txt").readLines().join("\n")
    }
}

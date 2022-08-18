package dev.siliconcode.gradle.activejdbc

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaExecSpec
import org.gradle.process.internal.ExecException
import org.javalite.instrumentation.Main

/**
 * Gradle task for performing ActiveJDBC instrumentation on a set of compiled {@code .class} files.
 */
class ActiveJDBCInstrumentation extends DefaultTask {

    /** The directory containing class files to be instrumented. */
    @Input
    String classesDir

    /** The output directory to write back classes after instrumentation. */
//    @Input
//    String outputDir

    private FileCollection activeJdbcClasspath

    /**
     * Class path holding the ActiveJDBC library.
     *
     * @return class path holding the ActiveJDBC library
     */
    @Classpath
    FileCollection getActiveJdbcClasspath() {
        if (!activeJdbcClasspath) {
            activeJdbcClasspath = project.configurations.getByName("activejdbc")
        }
        return activeJdbcClasspath
    }

    /**
     * @param activeJdbcClasspath
     *            class path holding the ActiveJdbc library
     */
    void setActiveJdbcClasspath(FileCollection activeJdbcClasspath) {
        this.activeJdbcClasspath = activeJdbcClasspath
    }

    ActiveJDBCInstrumentation() {
        description = "Instrument compiled class files extending from 'org.javalite.activejdbc.Model'"
    }

    @TaskAction
    def instrument() {
        logger.debug "ActiveJDBCInstrumentation.instrument"

        FileCollection instrumentationClasspath = getActiveJdbcClasspath()

        if (!classesDir) {
            File javaMainOutputDir = GradleUtils.getJavaMainOutputDir(project)
            classesDir = javaMainOutputDir.getPath()
            instrumentationClasspath += project.files(javaMainOutputDir)
        }
        else {
            instrumentationClasspath += project.files(classesDir)
        }

        logger.info "Instrumenting ActiveJDBC model classes under $classesDir"

//        runInstrumentation(instrumentationClasspath, outputDir ?: classesDir)
        runInstrumentation(instrumentationClasspath, classesDir)
    }

    private void runInstrumentation(FileCollection instrumentationClasspath, String outputDirpath) {
        String slf4JLogLevel = getSlf4LogLevel()

        try {
            project.javaexec { JavaExecSpec jes ->
                logger.info "Running ActiveJDBC instrumentation instance with environment: ${jes.environment}"

                jes.classpath = instrumentationClasspath
                jes.mainClass = Main.canonicalName
                jes.systemProperties = ['outputDirectory'                       : outputDirpath,
                                        'org.slf4j.simpleLogger.defaultLogLevel': slf4JLogLevel]

                jes.standardOutput = System.out
                jes.errorOutput = System.err
            }
        }
        catch (ExecException ex) {
            throw ex
        }
        finally {

        }
    }

    private String getSlf4LogLevel() {
        if (logger.isTraceEnabled()) return 'trace'
        if (logger.isDebugEnabled()) return 'debug'
        if (logger.isInfoEnabled()) return 'info'
        if (logger.isWarnEnabled()) return 'warn'
        if (logger.isErrorEnabled()) return 'error'
        return 'off'
    }
}

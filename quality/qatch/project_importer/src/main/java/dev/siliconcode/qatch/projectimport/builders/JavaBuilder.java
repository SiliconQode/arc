package dev.siliconcode.qatch.projectimport.builders;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tool to select and execute a java compilation process (using either the javac, maven, or gradle
 * tools) to ensure that Java analysis tools can be executed on the project
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class JavaBuilder {

  /** The base path of the project */
  protected final String basePath;
  /** The command line for executing the build tool */
  protected CommandLine cmdLine;

  private final String mvnHome;
  private final String gradleHome;
  private List<String> result = Lists.newArrayList();

  /**
   * Constructs a new ExecJavaCompiler with the given base path, maven home, and gradle home
   * configuration
   *
   * @param basePath Base path of the project to be analyzed, cannot be null or empty
   * @param mvnHome Location of the maven executable, cannot be null or empty
   * @param gradleHome Location of the gradle executable, cannot be null or empty
   */
  public JavaBuilder(
          @NonNull String basePath,
          @NonNull String mvnHome,
          @NonNull String gradleHome) {
    if (basePath.isEmpty()) throw new IllegalArgumentException("Base path cannot be empty");
    if (gradleHome.isEmpty()) throw new IllegalArgumentException("Gradle home cannot be empty");
    if (mvnHome.isEmpty()) throw new IllegalArgumentException("Maven home cannot be empty");

    this.basePath = basePath;
    this.mvnHome = mvnHome;
    this.gradleHome = gradleHome;
  }

  /**
   * Using data from the project folder this method selects the appropriate build tool
   * and then executes it within the project directory.
   */
  public void selectAndExecuteBuilder() {
    Path path = Paths.get(basePath);
    if (!Files.isDirectory(path)) return;

    try (Stream<Path> walk = Files.walk(path)) {
      result =
              walk.filter(Files::isReadable)
                      .filter(Files::isRegularFile)
                      .map(Path::getFileName)
                      .map(Path::toString)
                      .filter(s -> s.equals("build.gradle") || s.equals("pom.xml"))
                      .collect(Collectors.toList());
    } catch (IOException ex) {

    }

    if (result.contains("build.gradle")) runGradle();
    else if (result.contains("pom.xml")) runMaven();
    else runJavaC();

    execute(path.toAbsolutePath().toFile());
  }

  /**
   * Executes the process of compilation
   *
   * @param workingDir The directory in which the execution of the build tool starts
   */
  private void execute(@NonNull File workingDir) {
    if (cmdLine == null) {
      return;
    }

    try {
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(workingDir);
      executor.setExitValue(0);
      int exitValue = executor.execute(cmdLine);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /** Executes the javac compiler to build the project */
  private void runJavaC() {
    cmdLine = new CommandLine("javac");
    try {
      List<String> files = findJavaFiles();
      files.forEach(cmdLine::addArgument);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

  /** Executes gradle to build the project */
  private void runGradle() {
    cmdLine =
            new CommandLine(Paths.get(gradleHome, "bin/gradle").toString())
                    .addArgument("clean")
                    .addArgument("build");
    //            .addArgument("-x")
    //            .addArgument("test");
  }

  /** Executes maven to build the project */
  private void runMaven() {
    cmdLine =
            new CommandLine(Paths.get(mvnHome, "bin/mvn").toAbsolutePath().toString())
                    .addArgument("clean")
                    .addArgument("compile")
                    .addArgument("package")
                    .addArgument("-Dmaven.test.skip=true");
  }

  /**
   * Detects the location of java files to be compiled
   *
   * @return List of path strings for all java files within the project to be compiled
   * @throws IOException If the base path of the project cannot be traversed
   */
  private List<String> findJavaFiles() throws IOException {
    Path path = Paths.get(basePath);
    List<String> result = Lists.newArrayList();
    if (Files.exists(path) && Files.isDirectory(path)) {
      try (Stream<Path> walk = Files.walk(path)) {
        result =
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .map(Path::toString)
                        .collect(Collectors.toList());
      }
    }
    return result;
  }
}

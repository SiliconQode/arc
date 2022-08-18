package dev.siliconcode.qatch.projectimport.generators;

import dev.siliconcode.qatch.projectimport.ProjectGenerator;
import dev.siliconcode.qatch.projectimport.Submission;
import dev.siliconcode.qatch.projectimport.builders.JavaBuilder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * For a given set of submissions this class sets up a workspace for a Java analysis
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class JavaProjectGenerator extends ProjectGenerator {

  /** {@inheritDoc} */
  @Override
  public void generate(String workspace, List<Submission> submissions) {
    Path path = Paths.get(workspace, "java").toAbsolutePath().normalize();
    submissions.forEach(sub -> createDirectoryStructure(path.toString(), sub));
  }

  /** {@inheritDoc} */
  @Override
  public void createDirectoryStructure(String workspace, Submission submission) {
    Path projDir = Paths.get(workspace, submission.id()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(projDir);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
    createFiles(projDir.toString(), submission);
    JavaBuilder builder = new JavaBuilder(projDir.toString(), System.getenv("MAVEN_HOME"), System.getenv("GRADLE_HOME"));
    builder.selectAndExecuteBuilder();
  }

  /** {@inheritDoc} */
  @Override
  public void createFiles(String base, Submission submission) {
    Path build = Paths.get(base, "build.gradle").toAbsolutePath().normalize();
    Path settings = Paths.get(base, "settings.gradle").toAbsolutePath().normalize();
    Path solution = Paths.get(base, "src", "main", "java", "codesignal", "Solution.java").toAbsolutePath().normalize();

    try {
      Files.createFile(build);
      Files.createFile(settings);
      Files.createDirectories(solution.getParent());
      Files.createFile(solution);

      StringBuilder builder = new StringBuilder();
      builder.append("plugins {\n");
      builder.append("    id 'java'\n");
      builder.append("}\n");
      builder.append("\n");
      builder.append("repositories {\n");
      builder.append("    mavenCentral()\n");
      builder.append("}\n");
      builder.append("\n");
      builder.append("dependencies {\n");
      builder.append(getDefaultDependencies("java").indent(4));
      builder.append("\n}");
      String buildContents = builder.toString();

      builder = new StringBuilder();
      builder.append("rootProject.name = '").append(submission.id()).append("'");
      String settingsContents = builder.toString();

      builder = new StringBuilder();
      builder.append("package codesignal;\n");
      builder.append("\n");
      builder.append(getStandardImports("java"));
      builder.append("\n\n");
      builder.append("public class Solution {\n");
      builder.append(submission.source());
      builder.append("\n}");
      String solutionContents = builder.toString();

      Files.writeString(build, buildContents);
      Files.writeString(settings, settingsContents);
      Files.writeString(solution, solutionContents);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }
}

package dev.siliconcode.qatch.projectimport.generators;

import dev.siliconcode.qatch.projectimport.ProjectGenerator;
import dev.siliconcode.qatch.projectimport.Submission;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * For a given set of submissions this project generator sets up the workspace to
 * be correct for a python analysis
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class PythonProjectGenerator extends ProjectGenerator {

  /** {@inheritDoc} */
  @Override
  public void generate(String workspace, List<Submission> submissions) {
    Path path = Paths.get(workspace, "python").toAbsolutePath().normalize();
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
  }

  /** {@inheritDoc} */
  @Override
  public void createFiles(String base, Submission submission) {
    Path init = Paths.get(base, "__init__.py").toAbsolutePath().normalize();
    Path solution = Paths.get(base, "solution.py").toAbsolutePath().normalize();

    try {
      Files.createFile(init);
      Files.createFile(solution);

      String solutionContents = getStandardImports("python");
      solutionContents += "\n\n";
      solutionContents += submission.source();

      Files.writeString(solution, solutionContents);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }
}

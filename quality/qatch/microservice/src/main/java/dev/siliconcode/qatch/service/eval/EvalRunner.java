package dev.siliconcode.qatch.service.eval;

import dev.siliconcode.qatch.service.Config;
import dev.siliconcode.qatch.service.ServerContext;
import dev.siliconcode.qatch.service.data.Project;
import dev.siliconcode.qatch.service.data.Results;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tool to execute the Qatch Evaluation Runner for a given project
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class EvalRunner {

  /**
   * Executes the Qatch Evaluation for the provided project, if the provided project is not null
   *
   * @param proj The project to be evaluated
   */
  public void execute(@NonNull Project proj) throws IllegalArgumentException {
    ServerContext context = ServerContext.instance();
    Config config = context.getConfig();
    // Do all of this in a new Thread

    Path runnerPath = Paths.get(context.getConfig().getRunnerBinPath(), "qatch");
    CommandLine cmdLine = new CommandLine(runnerPath.toAbsolutePath().toString());
    cmdLine.addArgument("-sk");
    cmdLine.addArgument("-l=" + proj.language());
    cmdLine.addArgument(
        "-q=" + Paths.get(context.getConfig().getQmPath(), proj.language() + "QmModel.xml"));
    cmdLine.addArgument("-r=" + Paths.get(context.getConfig().getAnalysisPath()).toAbsolutePath());
    cmdLine.addArgument("-o=" + Paths.get(context.getConfig().getOutputPath()).toAbsolutePath());
    cmdLine.addArgument("-w=" + proj.path());

    try {
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(new File(proj.path()));
      executor.setExitValue(0);

      int exitValue = executor.execute(cmdLine);
    } catch (IOException ex) {

    }

    log.info("Extracting Results");
    Path resultsPath = Paths.get(context.getConfig().getOutputPath(), proj.name());
    extractResults(context.getConfig().getOutputPath(), proj);
  }

  /**
   * Extracts the results form the provided path, for the given project. Will throw an
   * IllegalArgumentException if either parameter is null or the path is empty
   *
   * @param path The path to the results file to extract results from
   * @param proj Project to which the results belong
   * @throws IllegalArgumentException if the provided path is empty
   */
  public void extractResults(@NonNull String path, @NonNull Project proj) {
    if (path.isEmpty()) throw new IllegalArgumentException("The provided path cannot be empty");

    Path p = Paths.get(path, proj.name() + "_evalResults.json").toAbsolutePath();
    log.info("Extracting results from: " + p);

    Results.fromJson(p.toString());
  }
}

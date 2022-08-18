package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

/**
 * Responsible for analyzing a single project agains:
 *
 * <ol>
 *  <li>a certain ruleset (i.e. property) or</li>
 *  <li>a set of rulesets (i.e. properties)</li>
 * </ol>
 * by invoking the pylint tool
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class PylintAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "pylint";

  /**
   * Constructs a new PylintAnalyzer instance
   *
   * @param pylintPath the path to the pylint tool, cannot be null
   * @param resultsPath path to the result directory, cannot be null
   * @param ruleSetPath to the rulesets, cannot be null
   */
  public PylintAnalyzer(@NonNull String pylintPath, @NonNull String resultsPath, @NonNull String ruleSetPath) {
    super(pylintPath, resultsPath, ruleSetPath);
  }

  /** {@inheritDoc} */
  protected CommandLine constructCommandLine(
      @NotNull String src,
      @NotNull String dest,
      @NotNull String ruleset,
      @NotNull String filename) {
    var rulesetName = Paths.get(ruleset).getFileName().toString().split("\\.")[0];
    CommandLine cmdLine = new CommandLine(toolPath)
            .addArgument("--rcfile=" + ruleset)
            .addArgument("--output-format=json:" + Paths.get(dest, filename + ".json"))
            .addArgument("-j")
            .addArgument("4")
            .addArgument(src);

    return cmdLine;
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }

  /** {@inheritDoc} */
  @Override
  protected int[] getExitValues() {
    int[] values = new int[64];
    for (int i = 0; i < values.length; i++)
      values[i] = i;
    return values;
  }
}

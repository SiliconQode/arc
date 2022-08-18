package dev.siliconcode.qatch.analyzers.kotlin;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

/**
 * Responsible for analyzing a single project against:
 *
 * <ol>
 *  <li>a certain ruleset (i.e. property) or</li>
 *  <li>a set of rulesets (i.e. properties)</li>
 * </ol>
 * by invoking the Detekt tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class DetektAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "detekt";

  /**
   * Constructs a new DetektAnalyzer instance
   *
   * @param path Path of the detekt tool, cannot be null
   * @param resultsPath Path to the results directory, cannot be null
   * @param rulesetPath Path tot he rulesets, cannot be null
   */
  public DetektAnalyzer(@NonNull String path, @NonNull String resultsPath, @NonNull String rulesetPath) {
    super(path, resultsPath, rulesetPath);
  }

  /** {@inheritDoc} */
  @Override
  protected CommandLine constructCommandLine(@NonNull String src, @NonNull String dest, @NonNull String ruleset, @NonNull String filename) {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }

  /** {@inheritDoc} */
  @Override
  protected int[] getExitValues() {
    return new int[0];
  }
}

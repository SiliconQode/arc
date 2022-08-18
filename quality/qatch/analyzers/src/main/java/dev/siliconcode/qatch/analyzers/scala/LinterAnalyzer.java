package dev.siliconcode.qatch.analyzers.scala;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

/**
 * Responsible for analyzing a single project against:
 *
 * <ol>
 *  <li>a certain rulset (i.e. property) or</li>
 *  <li>a set of rulesets (i.e. properties)</li>
 * </ol>
 * by invoking the scala linter tool
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class LinterAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "linter";

  /**
   * Consturcts a new instance of LinterAnalyzer
   *
   * @param linterPath path to the linter tool, cannot be null
   * @param resultsPath path to the results directory, cannot be null
   * @param ruleSetPath path to the rulesets, cannot be null
   */
  public LinterAnalyzer(@NonNull String linterPath, @NonNull String resultsPath, @NonNull String ruleSetPath) {
    super(linterPath, resultsPath, ruleSetPath);
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

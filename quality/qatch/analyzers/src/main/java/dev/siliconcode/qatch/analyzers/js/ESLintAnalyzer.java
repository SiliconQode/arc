package dev.siliconcode.qatch.analyzers.js;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import dev.siliconcode.qatch.analyzers.IssuesAnalyzer;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

/**
 * Responsible for analyzing a single project agains:
 *
 * <ol>
 *  <li>a certain ruleset (i.e. property) or</li>
 *  <li>a set of rulesets (i.e. properties)</li>
 * </ol>
 * by invoking the ESLint tool
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class ESLintAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "ESLint";

  /**
   * Constructs a new ESLintAnalyzer
   *
   * @param path Path to eslint executable, cannot be null
   * @param resultsPath path to the results directory, cannot be null
   * @param rulesetPath path to the rulesets, cannot be null
   */
  public ESLintAnalyzer(@NonNull String path, @NonNull String resultsPath, @NonNull String rulesetPath) {
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
    return null;
  }

  /** {@inheritDoc} */
  @Override
  protected int[] getExitValues() {
    return new int[0];
  }
}

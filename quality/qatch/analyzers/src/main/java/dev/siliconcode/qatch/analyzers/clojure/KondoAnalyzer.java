package dev.siliconcode.qatch.analyzers.clojure;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

/**
 * This class is responsible for analyzing a single project against:
 *
 * <ol>
 *   <li>a certain ruleset (i.e., property), or</li>
 *   <li>a set of rulesets (i.e., properties) by invoking the Kondo tool.</li>
 * </ol>
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class KondoAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "clj-kondo";

  /**
   * constructs a new KondoAnalyzer
   *
   * @param path Path of the project to be analyzed, cannot be null
   * @param resultsPath Path to the results directory, cannot be null
   * @param rulesetPath Path to the rulesets, cannot be null
   */
  public KondoAnalyzer(@NonNull String path, @NonNull String resultsPath, @NonNull String rulesetPath) {
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

  /* {@inheritDoc} */
  @Override
  protected int[] getExitValues() {
    return new int[0];
  }
}

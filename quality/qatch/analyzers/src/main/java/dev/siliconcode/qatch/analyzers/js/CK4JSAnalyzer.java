package dev.siliconcode.qatch.analyzers.js;

import dev.siliconcode.qatch.analyzers.AbstractMetricsAnalyzer;
import lombok.NonNull;
import org.apache.commons.exec.CommandLine;

/**
 * Responsible for analyzing a single project by invoking the CK4JS tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public class CK4JSAnalyzer extends AbstractMetricsAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "CK4JS";

  /**
   * Constructs a new CK4JSAnalyzer
   *
   * @param ck4jsPath Path to the directory containing the ck4js executable
   * @param resultPath Path to the tool result directory
   */
  public CK4JSAnalyzer(String ck4jsPath, String resultPath) {
    super(ck4jsPath, resultPath);
  }

  /** {@inheritDoc} */
  @Override
  protected CommandLine constructCommandLine(@NonNull String src, @NonNull String dest) {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }
}

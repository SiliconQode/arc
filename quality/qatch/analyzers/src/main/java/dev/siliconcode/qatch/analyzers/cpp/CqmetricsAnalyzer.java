package dev.siliconcode.qatch.analyzers.cpp;

import dev.siliconcode.qatch.analyzers.AbstractMetricsAnalyzer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

/**
 * Responsible for analyzing a single project by invoking the cqmetrics tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CqmetricsAnalyzer extends AbstractMetricsAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "cqmetrics";

  /**
   * Constructs a new CqmetricsAnalyzer
   *
   * @param cqmetricsPath Path to directory containing the cqmetrics tool's executable
   * @param resultPath Path to the tool results directory
   */
  public CqmetricsAnalyzer(String cqmetricsPath, String resultPath) {
    super(cqmetricsPath, resultPath);
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

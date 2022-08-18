package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.AbstractMetricsAnalyzer;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;

import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Responsible for analyzing a single project yb invoking the CKPM tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CKPMAnalyzer extends AbstractMetricsAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "CKPM";

  /**
   * Constructs a new instance of CKPMAnalyzer
   *
   * @param ckpmPath path to the ckpm tool, cannot be null
   * @param resultsPath the path to the results directory, cannot be null
   */
  public CKPMAnalyzer(@NonNull String ckpmPath, @NonNull String resultsPath) {
    super(ckpmPath, resultsPath);
  }

  /** {@inheritDoc} */
  @Override
  protected CommandLine constructCommandLine(@NonNull String src, @NonNull String dest) {
    return new CommandLine(Paths.get(toolPath, "ck4py").toAbsolutePath().normalize().toString())
          .addArgument("-d")
          .addArgument(src)
          .addArgument("-j")
          .addArgument("-n")
          .addArgument("-o")
          .addArgument(dest);
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }
}

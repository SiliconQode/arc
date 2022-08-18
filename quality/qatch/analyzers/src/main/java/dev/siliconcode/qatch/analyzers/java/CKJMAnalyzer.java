package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.AbstractMetricsAnalyzer;
import dev.siliconcode.qatch.analyzers.MetricsAnalyzer;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Responsible for analyzing a single project by invoking the CKJM tool.
 *
 * @author Miltos, Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CKJMAnalyzer extends AbstractMetricsAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "CKJM";

  /**
   * Constructs a new CKJMAnalyzer
   *
   * @param path Path to the directory containing the CKJM executable
   * @param resultPath Path to the tool results directory
   */
  public CKJMAnalyzer(String path, String resultPath) {
    super(path, resultPath);
  }

  /** {@inheritDoc} */
  @Override
  public CommandLine constructCommandLine(@NonNull String src, @NonNull String dest) {
    return new CommandLine(toolPath)
            .addArgument("-x")
            .addArgument("-n")
            .addArgument("-d")
            .addArgument(src)
            .addArgument("-o")
            .addArgument(dest);
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }
}

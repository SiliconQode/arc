package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.AbstractIssueAnalyzer;
import dev.siliconcode.qatch.analyzers.IssuesAnalyzer;
import dev.siliconcode.qatch.core.model.Finding;
import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Responsible for analyzing a single project against:
 *
 * <ol>
 *  <li>a certain ruleset (i.e. property) or</li>
 *  <li>a set of rulesets (i.e. properties)</li>
 * </ol>
 * by invoking the PMD tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class PMDAnalyzer extends AbstractIssueAnalyzer {

  /** Name of the tool for use in quality model definitions */
  public static final String TOOL_NAME = "PMD";

  /**
   * Constructs a new PMDAnalyzer
   *
   * @param path Path of the pmd tool, cannot be null
   * @param resultsPath Path to the results directory, cannot be null
   * @param rulesetPath Path to the rulesets, cannot be null
   */
  public PMDAnalyzer(
      @NonNull String path, @NonNull String resultsPath, @NonNull String rulesetPath) {
    super(path, resultsPath, rulesetPath);
  }

  /** {@inheritDoc} */
  @Override
  protected CommandLine constructCommandLine(
      @NotNull String src,
      @NotNull String dest,
      @NotNull String ruleset,
      @NotNull String filename) {

    return new CommandLine(toolPath)
      .addArgument("pmd")
      .addArgument("-d")
      .addArgument(src)
      .addArgument("-f")
      .addArgument("xml")
      .addArgument("-l")
      .addArgument("java")
      .addArgument("-R")
      .addArgument(ruleset)
      .addArgument("-r")
      .addArgument(dest + File.separator + filename + ".xml");
  }

  /** {@inheritDoc} */
  @Override
  public String getToolName() {
    return TOOL_NAME;
  }

  /** {@inheritDoc} */
  @Override
  protected int[] getExitValues() {
    return new int[]{0, 1, 4};
  }
}

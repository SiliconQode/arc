package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.core.model.Property;
import dev.siliconcode.qatch.core.model.PropertySet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Abstract class which defines the common capabilities of all metrics analyzers
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public abstract class AbstractMetricsAnalyzer implements MetricsAnalyzer {

  /** the path to the tool */
  protected String toolPath;
  /** the path to the results directory */
  protected String resultPath;

  /**
   * Constructs a new AbstractMetricsAnalyzer with the given path to the analyzer
   * tool and the directory to which results are to be stored.
   *
   * @param path the path to the tool executable
   * @param resultPath the path to the results directory
   */
  public AbstractMetricsAnalyzer(String path, String resultPath) {
    this.toolPath = path;
    this.resultPath = resultPath;
  }

  /**
   * This method is used to analyze a single project with the metrics static analysis tool.
   *
   * @param src The path of the folder that contains the class files of the project, cannot be  null
   * @param dest The path where the XML file that contains the results will be placed, cannot be null
   */
  @Override
  public void analyze(@NonNull String src, @NonNull String dest) {
    src = Paths.get(src).toAbsolutePath().normalize().toString();
    dest = Paths.get(dest).toAbsolutePath().normalize().toString();

    CommandLine cmdLine = constructCommandLine(src,  dest);

    try {
      DefaultExecutor executor = new DefaultExecutor();
      executor.setExitValue(0);
      executor.setWorkingDirectory(new File(src));
      int exitValue = executor.execute(cmdLine);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * This method is responsible for analyzing a single project against a set of
   * properties by using the configured metrics Tool.
   *
   * <p>
   * Typically this method does the following:
   * <ul>
   *  <li>Iterates through the PropertySet.</li>
   *  <li>If it finds at least one property that uses the tool (based on detecting)
   *      the tool name in the model defintiion) then it calls the simple analyze()
   *      method.
   * </ul>
   *    *
   * @param src The path of the folder that contains the class files of the desired project, cannot
   *     be null
   * @param dest The path where the XML file that contains the results should be placed, cannot be
   *     null
   * @param properties The set of properties to evaluate against, cannot be null
   */
  public void analyze(@NotNull String src, @NotNull String dest, @NotNull PropertySet properties) {
    Iterator<Property> iterator = properties.iterator();
    Property p;

    while (iterator.hasNext()) {
      p = iterator.next();

      boolean result = checkIsTool(p);
      if (result) {
        analyze(src, dest);
        break;
      }
    }
  }

  /**
   * Checks if the provided property is measured by the associated tool for this
   * analyzer
   *
   * @param p Property to evaluate
   * @return true if the provided property's measure uses the associated tool and
   *         measure is also a metric, false otherwise.
   */
  protected boolean checkIsTool(Property p) {
    return p.getMeasure().getTool().equals(getToolName()) && p.getMeasure().isMetric();
  }

  /**
   * Constructs the command line used to execute the associated analysis tool.
   *
   * @param src The directory in which the tool is to execute
   * @param dest The directory to which the tool results should be stored
   * @return the CommandLine object that will be used to execute the tool
   */
  protected abstract CommandLine constructCommandLine(@NonNull String src, @NonNull String dest);
}

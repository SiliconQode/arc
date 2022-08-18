package dev.siliconcode.qatch.calibration.io;

import dev.siliconcode.qatch.core.eval.Project;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for exporting the results of the benchmark analysis and aggregation in
 * different file formats, so that they can be used by R in order to calculate the thresholds of
 * each property.
 *
 * <p>More specifically, it does the following:
 *
 * <ol>
 *  <li>Receives a set of projects as an input. (The projects of the project set should have their
 *      normValue field for each Property object of their PropertySet calculated)</li>
 *  <li>It creates the appropriate file (e.g. json) inside the results directory</li>
 *  <li>It iterates through all the properties of each project of the project set and writes
 *      their normValue values.
 * </ol>
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class BenchmarkAnalysisExporter {

  /**
   * A method for exporting only the properties of the projects of a set of projects in a json
   * format. Typically, it exports only the PropertySet object of each Project found in a property
   * set (object of class BenchmarkProjects).
   *
   * <p>USAGES: - It can be used for debugging purposes. Use it instead of exporting everything
   * found in a BenchmarkProject object. (more lightweight) - As an example (tutorial), on how to
   * create your own json file.
   *
   * @param projects The BenchmarkProject to export, cannot be null
   * @param path The path to export to, cannot be null or empty
   */
  public void exportToJSON(@NonNull List<Project> projects, @NonNull String path) {
    if (path.isEmpty()) throw new IllegalArgumentException("Path cannot be empty");

    // Create the json parser
    Gson gson = new Gson();

    // Create the string containing the total json file
    String totalJson = "{\"projects\":[";

    // Iterate through the projects
    for (int i = 0; i < projects.size(); i++) {

      Project project = projects.get(i);
      String json = gson.toJson(project.getProperties());
      totalJson += json + ",";
    }

    // Close the json file appropriately
    totalJson += "]}";

    // Save the json file inside R Working Directory for Manipulation
    try {
      FileWriter writer = new FileWriter(path);
      writer.write(totalJson);
      writer.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}

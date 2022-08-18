package dev.siliconcode.qatch.service.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * A model object representing the results of a quality analysis
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Slf4j
public class Results extends Model {

  /** Empty constructor, needed by ActiveJDBC */
  public Results() {}

  /**
   * Constructs a new Results object with the provided data
   *
   * @param submissionID Submission ID of the project for which these results belong, cannot be null
   * @param tqi The total quality index
   * @param maintainability Maintainability results for the project
   * @param reliability Reliability results for the project
   * @param security Security results for the project
   * @param efficiency Performance Efficiency for the project
   */
  @Builder(buildMethodName = "create")
  public Results(
      @NonNull String submissionID,
      double tqi,
      double maintainability,
      double reliability,
      double security,
      double efficiency) {
    set(
        "submissionId",
        submissionID,
        "maintainability",
        maintainability,
        "reliability",
        reliability,
        "efficiency",
        efficiency,
        "security",
        security,
        "tqi",
        tqi);
    save();
  }

  /**
   * Convenience method to retrieve the maintainbility score. Note you should have an open database
   * connection
   *
   * @return Maintainability score
   */
  public double maintainability() {
    return getDouble("maintainability");
  }

  /**
   * Convenience method to retrieve the reliability score. Note you should have an open database
   * connection.
   *
   * @return Reliability score
   */
  public double reliability() {
    return getDouble("reliability");
  }

  /**
   * Convenience method to retrieve the security score. Note you should have an open database
   * connection.
   *
   * @return Security score
   */
  public double security() {
    return getDouble("security");
  }

  /**
   * Convenience method to retrieve the performance-efficiency score. Note you should have an open
   * database connection.
   *
   * @return Performance-Efficiency score
   */
  public double efficiency() {
    return getDouble("efficiency");
  }

  /**
   * Constructs a map containing all the fields of this object (can be used when constructing a JSON
   * representation or similar)
   *
   * @return Map of all data in this object
   */
  public Map<String, String> results() {
    Map<String, String> results = Maps.newHashMap();
    results.put("submissionId", getString("submissionId"));
    results.put("maintainability", Double.toString(maintainability()));
    results.put("reliability", Double.toString(reliability()));
    results.put("security", Double.toString(security()));
    results.put("efficiency", Double.toString(efficiency()));
    return results;
  }

  /**
   * Returns the results map for the Results object associated with the given submission id, if one
   * exists.
   *
   * @param submissionId The submission id of the project, cannot be null
   * @return A map containing all the data of a results object associated with the provided id, or
   *     an empty map if no such object exists
   * @throws IllegalArgumentException if the provided submission id is empty
   */
  public static Map<String, String> getResultsForId(@NonNull String submissionId)
      throws IllegalArgumentException {
    if (submissionId.isEmpty()) throw new IllegalArgumentException("Submission ID cannot be empty");
    Results res = Results.findFirst("submissionId = ?", submissionId);
    if (res != null) {
      return res.results();
    }
    return Maps.newHashMap();
  }

  /**
   * Constructs a new Results object from a provided file path
   *
   * @param pathStr String representing the path to the file
   * @return The new results object, or null if the file could not be read
   */
  public static Results fromJson(String pathStr) {
    Path path = Paths.get(pathStr);
    if (Files.notExists(path) || !Files.isRegularFile(path) || !Files.isReadable(path)) return null;

    Map<String, String> map = null;
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      Gson gson = new Gson();
      Type mapStringDouble = new TypeToken<Map<String, String>>() {}.getType();
      map = gson.fromJson(reader, mapStringDouble);
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    if (map != null) {
      DbManager.instance().open();
      Results results =
          new Results(
              map.get("submissionID"),
              Double.parseDouble(map.get("TQI")),
              Double.parseDouble(map.get("Maintainability")),
              Double.parseDouble(map.get("Reliability")),
              Double.parseDouble(map.get("Security")),
              Double.parseDouble(map.get("Performance_Efficiency")));
      DbManager.instance().close();

      return results;
    }

    return null;
  }
}

package dev.siliconcode.qatch.projectimport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import lombok.NonNull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to combine the data from a json submission data file and the json results files
 * for each submission, into a single csv file for analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class DataCombiner {

  /**
   * Main method which combines the data
   *
   * @param inputFile the input submission definition file, cannot be null
   * @param resultsDir the directory where the results for each analyzed submission exist, cannot be null
   * @param outputFile the path to the file in which the combined results are to be written, cannot be null
   */
  public void combineData(@NonNull String inputFile, @NonNull String resultsDir, @NonNull String outputFile) {
    Map<String, Results> results = readResults(resultsDir);
    Map<String, Submission> submissions = readSubmissions(inputFile, results.keySet());
    writeCombinedData(outputFile, submissions, results);
  }

  /**
   * Writes the combined data to a CSV file
   *
   * @param path The path to the output file
   * @param submissions a map of the submissions data
   * @param results a map of the results for considered submissions
   */
  public void writeCombinedData(String path, Map<String, Submission> submissions, Map<String, Results> results) {
    try (CSVPrinter printer = new CSVPrinter(new FileWriter(path), CSVFormat.DEFAULT)) {
      printer.printRecord(
              "Submission ID",
              "Candidate",
              "Language",
              "Score",
              "Maintainability",
              "Reliability",
              "Security",
              "Performance Efficiency",
              "Total Quality",
              "Good Code?"
      );

      for (String key : submissions.keySet()) {
        printer.printRecord(
                key,
                submissions.get(key).name(),
                submissions.get(key).lang(),
                submissions.get(key).score(),
                results.get(key).maintainability(),
                results.get(key).reliability(),
                results.get(key).security(),
                results.get(key).efficiency(),
                results.get(key).tqi(),
                submissions.get(key).goodCode()
        );
      }
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * Imports the results of all submissions in the provided directory, into a single map.
   *
   * @param path the path to the directory containing submission analysis results.
   * @return a map keyed by submission id, and containing the results object for that submission
   */
  public Map<String, Results> readResults(String path) {
    Map<String, Results> map = Maps.newHashMap();
    ObjectMapper mapper = new ObjectMapper();

    Path base = Paths.get(path);
    try(var stream = Files.newDirectoryStream(base)) {
      stream.forEach(file -> {
        if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".json")) {
          try {
            Map<String, Object> data = (Map<String, Object>) mapper.readValue(file.toFile(), Map.class);

            Results res = new Results(
                    (String) data.get("submissionID"),
                    Double.parseDouble((String) data.get("Maintainability")),
                    Double.parseDouble((String) data.get("Reliability")),
                    Double.parseDouble((String) data.get("Security")),
                    Double.parseDouble((String) data.get("Performance_Efficiency")),
                    Double.parseDouble((String) data.get("TQI"))
            );

            map.put(res.submissionId(), res);
          } catch (IOException e) {
            log.error(e.getMessage());
          }
        }
      });
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return map;
  }

  /**
   * Imports data from the provided file, and constructs a map of that submission data for the
   * selected set of submission ids
   *
   * @param path the path to the file containing submission data
   * @param submissionIds the set of all considered submission ids
   * @return A map of (submission Id, submission) pairs
   */
  public Map<String, Submission> readSubmissions(String path, Set<String> submissionIds) {
    Map<String, Submission> data = Maps.newHashMap();
    ObjectMapper mapper = new ObjectMapper();

    try {
      Path file = Paths.get(path).toAbsolutePath().normalize();
      List<Map<String, Object>> submissions = (List<Map<String, Object>>) mapper.readValue(file.toFile(), ArrayList.class);

      submissions.forEach(sub -> {
        if (submissionIds.contains((String) sub.get("submissionId"))) {
          Submission submission = new Submission((String) sub.get("submissionId"),
                (String) sub.get("candidateFullName"),
                (Integer) sub.get("score"),
                (String) sub.get("language"),
                (String) sub.get("source"),
                (String) sub.get("role"),
                (String) sub.get("rejectionReason"),
                (String) sub.get("stage"),
                (Boolean) sub.get("goodCode"));
          data.put(submission.id(), submission);
        }
      });
    } catch (Exception ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
    }

    return data;
  }
}

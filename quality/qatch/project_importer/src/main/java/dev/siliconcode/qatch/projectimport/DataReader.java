package dev.siliconcode.qatch.projectimport;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for reading in submission data and constructing a list of submissions for evaluation
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class DataReader {

  /**
   * Reads in all submissions and exports a list of submissions for the given language
   *
   * @param path the path to the submission data json file.
   * @param language the language to filter submissions by.
   * @return A list of submission objects representing the imported data.
   */
  public List<Submission> readData(@NonNull String path, @NonNull String language) {
    List<Submission> data = Lists.newArrayList();
    ObjectMapper mapper = new ObjectMapper();

    try {
      Path file = Paths.get(path).toAbsolutePath().normalize();
      List<Map<String, Object>> submissions = (List<Map<String, Object>>) mapper.readValue(file.toFile(), ArrayList.class);

      submissions.forEach(sub -> {
        if (Objects.equals(sub.get("language"), language)) {
          data.add(new Submission((String) sub.get("submissionId"),
                (String) sub.get("candidateFullName"),
                (Integer) sub.get("score"),
                (String) sub.get("language"),
                (String) sub.get("source"),
                (String) sub.get("role"),
                (String) sub.get("rejectionReason"),
                (String) sub.get("stage"),
                (Boolean) sub.get("goodCode")));
        }
      });
    } catch (Exception ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
    }

    return data;
  }
}

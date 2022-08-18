package dev.siliconcode.qatch.analyzers.python;

import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.Issue;
import dev.siliconcode.qatch.core.eval.IssueSet;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Imports the results of a pylint execution
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class PylintResultsImporter implements IssuesImporter {

  @Setter @Getter private String ruleSetName;

  /** {@inheritDoc} */
  @Override
  public IssueSet parseIssues(String path) {
    log.info("Importing pylint results from: " + path);

    IssueSet violations = new IssueSet();
    ObjectMapper mapper = new ObjectMapper();

    try {
      Path file = Paths.get(path).toAbsolutePath().normalize();
      violations.setPropertyName(file.getFileName().toString().split("\\.")[0]);
      List<Map<String, Object>> violationList = (List<Map<String, Object>>) mapper.readValue(file.toFile(), ArrayList.class);
      violationList.forEach(node -> {
        int line = (Integer) node.get("line");
        int endLine = node.get("endLine") == null ? line : (Integer) node.get("endLine");
        int col = node.get("col")== null ? 0 : (Integer) node.get("col");
        int endCol = node.get("endCol") == null ? col : (Integer) node.get("endCol");
        Issue issue =
                new Issue(
                        (String) node.get("message-id"),
                        ruleSetName,
                        fullyQualified((String) node.get("module"), (String) node.get("obj")),
                        (String) node.get("message"),
                        "",
                        mapPriority((String) node.get("type")),
                        line,
                        endLine,
                        col,
                        endCol,
                        "");
        violations.addIssue(issue);
      });
    } catch (Exception ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
    }
    return violations;
  }

  /**
   * Constructs the fully qualified name of the object
   *
   * @param module name of the module, cannot be null (but can be empty)
   * @param obj name of the object, cannot be null
   * @return the fully qualified name of the object
   */
  private String fullyQualified(@NonNull String module, @NonNull String obj) {
    return module + "." + obj;
  }

  /**
   * Maps pylint message types to a priority level.
   *
   * @param type The type of message to map
   * @return the priority level for the provided message type
   */
  private int mapPriority(String type) {
    return switch(type) {
      case "convention" -> 1;
      case "refactor" -> 2;
      case "warning" -> 3;
      case "error" -> 4;
      case "fatal" -> 5;
      default -> 1;
    };
  }
}

package dev.siliconcode.qatch.analyzers;

import dev.siliconcode.qatch.core.eval.IssueSet;

/**
 * Base interface for results importers for static analysis tools which report issues
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public interface IssuesImporter {

  /**
   * Method to parse the issues from the file with the given path
   *
   * @param path Path to the results file from which to parse the issues
   * @return IssuesSet representing the issues found
   */
  IssueSet parseIssues(String path);
}

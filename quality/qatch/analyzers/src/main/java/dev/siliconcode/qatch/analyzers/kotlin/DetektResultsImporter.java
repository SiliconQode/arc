package dev.siliconcode.qatch.analyzers.kotlin;

import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.IssueSet;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for importing all the violations reported by the Dekekt tool.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class DetektResultsImporter implements IssuesImporter {

  /** {@inheritDoc} */
  @Override
  public IssueSet parseIssues(String path) {
    return null;
  }
}

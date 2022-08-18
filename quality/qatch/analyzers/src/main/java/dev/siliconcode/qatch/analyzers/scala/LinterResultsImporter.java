package dev.siliconcode.qatch.analyzers.scala;

import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.IssueSet;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for importing all the violations resulting from a Linter analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class LinterResultsImporter implements IssuesImporter {

  /** {@inheritDoc} */
  @Override
  public IssueSet parseIssues(String path) {
    return null;
  }
}

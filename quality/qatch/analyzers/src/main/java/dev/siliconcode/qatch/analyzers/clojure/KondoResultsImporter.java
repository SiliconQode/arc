package dev.siliconcode.qatch.analyzers.clojure;

import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.IssueSet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * This class is responsible for importing all the violations reported by a Kondo analysis.
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class KondoResultsImporter implements IssuesImporter {

  /** {@inheritDoc} */
  @Override
  public IssueSet parseIssues(@NonNull String path) {
    return null;
  }
}

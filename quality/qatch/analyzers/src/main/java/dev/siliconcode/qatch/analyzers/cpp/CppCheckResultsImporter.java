package dev.siliconcode.qatch.analyzers.cpp;

import dev.siliconcode.qatch.analyzers.IssuesImporter;
import dev.siliconcode.qatch.core.eval.IssueSet;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Responsible for importing all the violations reported by a CppCheck Analysis
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
@Log4j2
public class CppCheckResultsImporter implements IssuesImporter {

  /** {@inheritDoc} */
  @Override
  public IssueSet parseIssues(@NonNull String path) {
    return null;
  }
}

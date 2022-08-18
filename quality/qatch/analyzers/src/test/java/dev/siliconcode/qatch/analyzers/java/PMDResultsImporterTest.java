package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.core.eval.IssueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

class PMDResultsImporterTest {

  private PMDResultsImporter pmdResultsImporterUnderTest;

  @BeforeEach
  void setUp() {
    pmdResultsImporterUnderTest = new PMDResultsImporter();
  }

  @Test
  void testParseIssues() {
    // Setup
    // Run the test
    final IssueSet result = pmdResultsImporterUnderTest.parseIssues("path");

    // Verify the results
  }

  @Test
  void testParseIssuesPerFile() {
    // Setup
    // Run the test
    final Vector<IssueSet> result = pmdResultsImporterUnderTest.parseIssuesPerFile("path");

    // Verify the results
  }
}

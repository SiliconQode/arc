package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.core.model.PropertySet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMDAnalyzerTest {

  private PMDAnalyzer pmdAnalyzerUnderTest;

  @BeforeEach
  void setUp() {
    pmdAnalyzerUnderTest = new PMDAnalyzer("path", "resultsPath", "rulesetPath");
  }

  @Test
  void testAnalyze1() {
    // Setup
    // Run the test
    pmdAnalyzerUnderTest.analyze("src", "dest", "ruleset", "filename");

    // Verify the results
  }

  @Test
  void testAnalyze2() {
    // Setup
    final PropertySet properties = new PropertySet();

    // Run the test
    pmdAnalyzerUnderTest.analyze("src", "dest", properties);

    // Verify the results
  }
}

package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.core.model.PropertySet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CKJMAnalyzerTest {

  private CKJMAnalyzer ckjmAnalyzerUnderTest;

  @BeforeEach
  void setUp() {
    ckjmAnalyzerUnderTest = new CKJMAnalyzer("path", "resultPath");
  }

  @Test
  void testAnalyze1() {
    // Setup
    // Run the test
    ckjmAnalyzerUnderTest.analyze("src", "dest");

    // Verify the results
  }

  @Test
  void testAnalyze2() {
    // Setup
    final PropertySet properties = new PropertySet();

    // Run the test
    ckjmAnalyzerUnderTest.analyze("src", "dest", properties);

    // Verify the results
  }
}

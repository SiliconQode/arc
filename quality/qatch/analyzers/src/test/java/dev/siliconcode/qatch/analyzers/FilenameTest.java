package dev.siliconcode.qatch.analyzers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilenameTest {

  private Filename filenameUnderTest;

  @BeforeEach
  void setUp() {
    filenameUnderTest = new Filename("str", 'a', 'a');
  }

  @Test
  void testExtension() {
    // Setup
    // Run the test
    final String result = filenameUnderTest.extension();

    // Verify the results
    assertEquals("result", result);
  }

  @Test
  void testFilename() {
    // Setup
    // Run the test
    final String result = filenameUnderTest.filename();

    // Verify the results
    assertEquals("result", result);
  }

  @Test
  void testPath() {
    // Setup
    // Run the test
    final String result = filenameUnderTest.path();

    // Verify the results
    assertEquals("result", result);
  }
}

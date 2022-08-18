package dev.siliconcode.qatch.analyzers.java;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaProviderTest {

  private JavaProvider javaProviderUnderTest;

  @BeforeEach
  void setUp() {
    javaProviderUnderTest = null /* TODO: construct the instance */;
  }

  @Test
  void testInitialize() {
    // Setup
    // Run the test
    javaProviderUnderTest.initialize("configPath", "resultsPath");

    // Verify the results
  }

  @Test
  void testGetLanguage() {
    assertEquals("java", javaProviderUnderTest.getLanguage());
  }

  @Test
  void testInstance() {
    // Setup
    // Run the test
    final LanguageProvider result = JavaProvider.instance();

    // Verify the results
  }
}

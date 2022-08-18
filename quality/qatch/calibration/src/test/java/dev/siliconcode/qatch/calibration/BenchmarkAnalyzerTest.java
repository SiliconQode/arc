package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.core.model.PropertySet;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class BenchmarkAnalyzerTest {

  @Mock private PropertySet mockProperties;
  private LanguageProvider provider;

  private BenchmarkAnalyzer benchmarkAnalyzerUnderTest;

  private AutoCloseable mockitoCloseable;

  @BeforeEach
  void setUp() {
    mockitoCloseable = openMocks(this);
    benchmarkAnalyzerUnderTest =
        new BenchmarkAnalyzer("benchRepoPath", "resultsPath", mockProperties, provider);
  }

  @AfterEach
  void tearDown() throws Exception {
    mockitoCloseable.close();
  }

  @Test
  void testAnalyzeBenchmarkRepo() {
    // Setup
    // Run the test
    benchmarkAnalyzerUnderTest.analyzeBenchmarkRepo();

    // Verify the results
  }
}

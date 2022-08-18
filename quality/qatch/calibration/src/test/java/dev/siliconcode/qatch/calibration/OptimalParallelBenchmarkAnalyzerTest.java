package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.core.model.PropertySet;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Vector;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class OptimalParallelBenchmarkAnalyzerTest {

  @Mock private PropertySet mockProperties;

  private OptimalParallelBenchmarkAnalyzer optimalParallelBenchmarkAnalyzerUnderTest;

  private LanguageProvider provider;

  private AutoCloseable mockitoCloseable;

  @BeforeEach
  void setUp() {
    mockitoCloseable = openMocks(this);
    optimalParallelBenchmarkAnalyzerUnderTest =
        new OptimalParallelBenchmarkAnalyzer("benchRepoPath", "resultsPath", mockProperties, provider);
  }

  @AfterEach
  void tearDown() throws Exception {
    mockitoCloseable.close();
  }

  @Test
  void testAnalyzeBenchmarkRepo() {
    // Setup
    when(mockProperties.iterator()).thenReturn(null);
    when(mockProperties.size()).thenReturn(0);

    // Run the test
    optimalParallelBenchmarkAnalyzerUnderTest.analyzeBenchmarkRepo();

    // Verify the results
  }
}

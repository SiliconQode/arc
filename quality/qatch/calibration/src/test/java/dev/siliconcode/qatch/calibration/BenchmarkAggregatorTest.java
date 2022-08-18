package dev.siliconcode.qatch.calibration;

import dev.siliconcode.qatch.analyzers.LanguageProvider;
import dev.siliconcode.qatch.core.eval.Project;
import dev.siliconcode.qatch.core.model.PropertySet;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class BenchmarkAggregatorTest {

  private BenchmarkAggregator benchmarkAggregatorUnderTest;
  private LanguageProvider provider;

  @BeforeEach
  void setUp() {
    benchmarkAggregatorUnderTest = new BenchmarkAggregator();
  }

  @Test
  void testAggregateProjects() {
    // Setup
    final List<Project> projects = Lists.newArrayList();
    final PropertySet properties = new PropertySet();

    // Run the test
    final List<Project> result =
        benchmarkAggregatorUnderTest.aggregateProjects(projects, properties, provider);

    // Verify the results
  }

  @Test
  void testNormalizeProperties() {
    // Setup
    final List<Project> projects = Lists.newArrayList();

    // Run the test
    benchmarkAggregatorUnderTest.normalizeProperties(projects);

    // Verify the results
  }
}

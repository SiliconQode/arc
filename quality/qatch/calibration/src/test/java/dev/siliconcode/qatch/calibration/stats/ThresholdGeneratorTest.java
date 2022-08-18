package dev.siliconcode.qatch.calibration.stats;

import dev.siliconcode.qatch.core.eval.Project;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ThresholdGeneratorTest {

  private ThresholdGenerator thresholdGeneratorUnderTest;

  @BeforeEach
  void setUp() {
    thresholdGeneratorUnderTest = new ThresholdGenerator();
  }

  @Test
  void testGetPropertyNames() {
    // Setup
    final List<Project> projects = Lists.newArrayList();

    // Run the test
    final String[] result = thresholdGeneratorUnderTest.getPropertyNames(projects);

    // Verify the results
    assertArrayEquals(new String[] {"result"}, result);
  }

  @Test
  void testGetProjectMeasures() {
    // Setup
    final List<Project> projects = Lists.newArrayList();
    final double[][] expectedResult = new double[][] {{0.0}};

    // Run the test
    final double[][] result = thresholdGeneratorUnderTest.getProjectMeasures(projects);

    // Verify the results
    assertArrayEquals(expectedResult, result);
  }

  @Test
  void testGenerateThresholds() {
    // Setup
    final List<Project> projects = Lists.newArrayList();
    final Map<String, Triple<Double, Double, Double>> expectedResult =
        Map.ofEntries(Map.entry("value", Triple.of(0.0, 0.0, 0.0)));

    // Run the test
    final Map<String, Triple<Double, Double, Double>> result =
        thresholdGeneratorUnderTest.generateThresholds(projects);

    // Verify the results
    assertEquals(expectedResult, result);
  }
}

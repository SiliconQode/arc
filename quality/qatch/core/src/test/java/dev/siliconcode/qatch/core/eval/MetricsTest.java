package dev.siliconcode.qatch.core.eval;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {

  private Metrics metricsUnderTest;

  @BeforeEach
  void setUp() {
    metricsUnderTest = new Metrics("className");
  }

  @Test
  void testGet() {
    // Setup
    metricsUnderTest.set("metric", 0.0);

    // Run the test
    final double result = metricsUnderTest.get("metric");

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void testGet_ThrowsIllegalArgumentException() {
    // Setup
    // Run the test
    assertThrows(IllegalArgumentException.class, () -> metricsUnderTest.get("metric"));
  }

  @Test
  void testSet() {
    // Setup
    // Run the test
    metricsUnderTest.set("metric", 0.0);

    // Verify the results
    assertEquals(0.0, metricsUnderTest.get("metric"), 0.0001);
  }

  @Test
  void testSet_ThrowsIllegalArgumentException() {
    // Setup
    // Run the test
    assertThrows(IllegalArgumentException.class, () -> metricsUnderTest.set("", 0.0));
  }

  @Test
  void testGettersSetters() {
    metricsUnderTest.setClassName("new class");
    metricsUnderTest.setMethods(Lists.newArrayList());

    assertEquals("new class", metricsUnderTest.getClassName());
    assertTrue(metricsUnderTest.getMethods().isEmpty());
  }
}

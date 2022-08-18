package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricTest {

  private Metric metricUnderTest;

  @BeforeEach
  void setUp() {
    metricUnderTest = new Metric("name", "tool");
  }

  @Test
  void testIsMetric() {
    assertTrue(metricUnderTest.isMetric());
  }

  @Test
  void testIsFinding() {
    assertFalse(metricUnderTest.isFinding());
  }

  @Test
  void testGetterSetter() {
    metricUnderTest.setMetricName("otherName");
    metricUnderTest.setTool("otherTool");

    assertEquals("otherName", metricUnderTest.getMetricName());
    assertEquals("otherTool", metricUnderTest.getTool());
  }
}

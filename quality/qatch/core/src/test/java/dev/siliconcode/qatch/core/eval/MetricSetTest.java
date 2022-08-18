package dev.siliconcode.qatch.core.eval;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MetricSetTest {

  private MetricSet metricSetUnderTest;
  private Metrics metrics;

  @BeforeEach
  void setUp() {
    metricSetUnderTest = new MetricSet();
    metrics = new Metrics("className");
    metricSetUnderTest.addMetrics(metrics);
  }

  @Test
  void testAddMetrics() {
    // Setup
    final Metrics metrics = new Metrics("className");

    // Run the test
    metricSetUnderTest.addMetrics(metrics);

    // Verify the results
    assertEquals(2, metricSetUnderTest.size());
  }

  @Test
  void testAddMetrics_NPE() {
    assertThrows(NullPointerException.class, () -> metricSetUnderTest.addMetrics(null));
  }

  @Test
  void testGet() {
    // Setup
    // Run the test
    final Metrics result = metricSetUnderTest.get(0);

    // Verify the results
    assertEquals(metrics, result);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = metricSetUnderTest.isEmpty();

    // Verify the results
    assertFalse(result);
  }

  @Test
  void testIterator() {
    // Setup
    // Run the test
    final Iterator<Metrics> result = metricSetUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    // Run the test
    final Stream<Metrics> result = metricSetUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testSize() {
    // Setup
    // Run the test
    final int result = metricSetUnderTest.size();

    // Verify the results
    assertEquals(1, result);
  }

  @Test
  void testToArray() {
    // Setup
    Metrics[] expected = {metrics};
    // Run the test
    final Metrics[] result = metricSetUnderTest.toArray();

    // Verify the results
    assertArrayEquals(expected, result);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = metricSetUnderTest.toString();

    // Verify the results
    assertEquals("[" + metrics.toString() + "]", result);
  }

  @Test
  void testGetMetricSet() {
    // Setup
    List<Metrics> metrics = new ArrayList<>();
    metricSetUnderTest.setMetricSet(metrics);

    // Run the test
    List<Metrics> results = metricSetUnderTest.getMetricSet();

    // verify the results
    assertEquals(metrics, results);
  }

  @Test
  void testSetMetricSet() {
    // Setup
    List<Metrics> metrics = new ArrayList<>();

    // Run the test
    metricSetUnderTest.setMetricSet(metrics);

    // verify the results
    assertEquals(metrics, metricSetUnderTest.getMetricSet());
  }
}

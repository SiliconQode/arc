package dev.siliconcode.qatch.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ThresholdsTest {

  private Thresholds thresholdsUnderTest;

  @BeforeEach
  void setUp() throws Exception {
    thresholdsUnderTest = new Thresholds(0);
  }

  @Test
  void testAdd1() {
    // Setup
    // Run the test
    thresholdsUnderTest.add(0.0);

    // Verify the results
    assertEquals(1, thresholdsUnderTest.size());
  }

  @Test
  void testAdd2() {
    // Setup
    // Run the test
    thresholdsUnderTest.add(0, 0.0);
    thresholdsUnderTest.add(0, 1.0);

    // Verify the results
    assertEquals(2, thresholdsUnderTest.size());
    assertEquals(1.0, thresholdsUnderTest.get(0), 0.0001);
  }

  @Test
  void testContains() {
    // Setup
    // Run the test
    final boolean result = thresholdsUnderTest.contains(0.0);

    // Verify the results
    assertFalse(result);
  }

  @Test
  void testGet() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    final Double result = thresholdsUnderTest.get(0);

    // Verify the results
    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void testIsEmpty() {
    // Setup
    // Run the test
    final boolean result = thresholdsUnderTest.isEmpty();

    // Verify the results
    assertTrue(result);
    thresholdsUnderTest.add(0.0);
    assertFalse(thresholdsUnderTest.isEmpty());
  }

  @Test
  void testIterator() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    final Iterator<Double> result = thresholdsUnderTest.iterator();

    // Verify the results
    assertTrue(result.hasNext());
    assertNotNull(result.next());
  }

  @Test
  void testStream() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    final Stream<Double> result = thresholdsUnderTest.stream();

    // Verify the results
    assertEquals(1, result.count());
  }

  @Test
  void testRemove1() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    thresholdsUnderTest.remove(0);

    // Verify the results
    assertTrue(thresholdsUnderTest.isEmpty());
  }

  @Test
  void testRemove2() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    thresholdsUnderTest.remove(0.0);

    // Verify the results
    assertTrue(thresholdsUnderTest.isEmpty());
  }

  @Test
  void testRemove2_NPE() {
    assertThrows(NullPointerException.class, () -> thresholdsUnderTest.remove(null));
  }

  @Test
  void testSize() {
    // Setup
    // Run the test
    final int result = thresholdsUnderTest.size();

    // Verify the results
    assertEquals(0, result);
  }

  @Test
  void testToArray() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    final Double[] result = thresholdsUnderTest.toArray();

    // Verify the results
    assertEquals(1, result.length);
  }

  @Test
  void testToString() {
    // Setup
    // Run the test
    final String result = thresholdsUnderTest.toString();

    // Verify the results
    assertEquals("[]", result);
  }

  @Test
  void testClear() {
    // Setup
    thresholdsUnderTest.add(0.0);

    // Run the test
    thresholdsUnderTest.clear();

    // Verify the results
    assertTrue(thresholdsUnderTest.isEmpty());
  }

  @Test
  void testAddAll() {
    // Setup
    // Run the test
    thresholdsUnderTest.addAll(0.0, 1.0);

    // Verify the results
    assertEquals(2, thresholdsUnderTest.size());
  }

  @Test
  void testAddAll_NPE() {
    assertThrows(NullPointerException.class, () -> thresholdsUnderTest.addAll((Double[]) null));
  }
}
